package org.xavier.blog.service.impl;

import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.FetchSourceFilter;
import org.springframework.data.elasticsearch.core.query.StringQuery;
import org.springframework.stereotype.Service;
import org.xavier.blog.dao.AccessRuleMapper;
import org.xavier.blog.dao.ArticleCategoryMapper;
import org.xavier.blog.dao.ArticleMapper;
import org.xavier.blog.dao.GroupRelationshipMapper;
import org.xavier.blog.domain.bo.ArticleSummaryQueryBO;
import org.xavier.blog.domain.po.*;
import org.xavier.blog.service.ArticleService;
import org.xavier.blog.task.bo.ArticleForElasticSearch;
import org.xavier.common.exception.Universal404Exception;
import org.xavier.webtoolkit.base.DefaultUtils;
import org.xavier.webtoolkit.domain.PageResult;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicReference;

/**
 * 描述信息：<br/>
 *
 * @author Xavier
 * @version 1.0
 * @date 2020-09-04
 * @since Jdk 1.8
 */
@Service
public class ArticleElasticSearchServiceImpl extends DefaultUtils implements ArticleService {

    /**
     * ES 查询结果排除 "content", "properties"
     */
    private static final FetchSourceFilter ARTICLE_SUMMARY_SOURCE_FILTER = new FetchSourceFilter(null, new String[]{"content"});
    /**
     * ES 查询结果按 _score 倒排(大到小)
     */
    private static final Sort SCORE_DESC_SORT = Sort.by(new Sort.Order(Sort.Direction.DESC, "_score"));

    @Autowired
    ArticleMapper articleMapper;
    @Autowired
    AccessRuleMapper accessRuleMapper;
    @Autowired
    UserServiceImpl userService;
    @Autowired
    BoardServiceImpl boardService;
    @Autowired
    ArticleCategoryServiceImpl articleCategoryService;
    @Autowired
    ArticleCategoryMapper articleCategoryMapper;
    @Autowired
    GroupRelationshipMapper groupRelationshipMapper;
    @Autowired
    GroupServiceImpl groupService;
    @Autowired
    ElasticsearchRestTemplate elasticsearchRestTemplate;

    public void updateArticleInElasticSearchAsync(Article article) {
        CompletableFuture.runAsync(() -> {
            elasticsearchRestTemplate.save(article);
        }).exceptionally((throwable) -> {
            logger.error("Fail to update es Article:" + jsonHelper.format(article), throwable);
            return null;
        });
    }

    public void updateArticleInElasticSearchAsync(String articleNo) {
        AtomicReference<Article> articleContainer = null;
        CompletableFuture.runAsync(() -> {
            articleContainer.set(articleMapper.queryArticleByArticleNo(articleNo));
            elasticsearchRestTemplate.save(articleContainer.get());
            logger.always("Update es Article(" + articleContainer.get().getTitle() + ") success.");
        }).exceptionally((throwable) -> {
            logger.error("Fail to update es Article:" + jsonHelper.format(articleContainer.get()), throwable);
            return null;
        });
    }

    /**
     * 文章全文检索(从 title summary content 匹配)
     */
    public PageResult<ArticleSummaryQueryBO> articleSearch(String loginUid, String secretKey, String keyword, Integer currentPage, Integer pageSize) throws Universal404Exception {
        PageResult<ArticleSummaryQueryBO> result = new PageResult<>();
        User loginUser = userService.queryUserNotNull(loginUid);
        ArrayList<String> allJoinedGroupTemp = groupRelationshipMapper.queryGroupIdListOfUser(loginUid);
        HashMap<String, Object> allJoinedGroup = collectionHelper.filterCollectionNotEmptyAsHashMap(allJoinedGroupTemp, "Item of [gno] can't be null.",
                (gno) -> gno,
                (gno) -> gno);
        ArrayList<ArticleCategory> totalArticleCategoryList = articleCategoryService.queryAllArticleCategory(null);
        // key-value articleCategoryNo-ArticleCategory
        HashMap<String, ArticleCategory> totalArticleCategoryMap = collectionHelper.filterCollectionNotEmptyAsHashMap(totalArticleCategoryList, "",
                (articleCategory) -> articleCategory.getArticleCategoryNo(),
                (articleCategory) -> articleCategory);
        ArrayList<String> needCheckArticleCategoryNoList = collectionHelper.filterCollectionNotEmptyAsArrayList(true, totalArticleCategoryList, "[totalArticleCategoryList] for query can't be empty.",
                (articleCategory) -> articleCategory.getArticleCategoryNo()
        );
        if (needCheckArticleCategoryNoList.size() < 1) {
            result.setTotalCount(0);
            result.setResultSet(new ArrayList<>(0));
            return result;
        }
        ArrayList<AccessRule> accessRuleList = accessRuleMapper.queryAccessRuleByArticleCategoryNoMultiple(needCheckArticleCategoryNoList);
        ArrayList<String> allowableArticleCategoryNoList = getAllowableArticleCategoryNoList(secretKey, loginUser, allJoinedGroup, accessRuleList);
        SearchHits<ArticleForElasticSearch> searchHits = queryFromElasticSearch(keyword, allowableArticleCategoryNoList, currentPage, pageSize);
        Integer totalCount = Long.valueOf(searchHits.getTotalHits()).intValue();
        if (totalCount != 0) {
            ArrayList<Board> allBoardList = boardService.queryAllBoardList(1, 999, "ts", false);
            HashMap<String, Board> allBoardMap = collectionHelper.filterCollectionNotEmptyAsHashMap(allBoardList, "", (board) -> board.getBoardNo(), (board -> board));
            ArrayList<ArticleSummaryQueryBO> resultSet = new ArrayList(pageSize);
            searchHits.forEach((currentSearchHits) -> {
                ArticleForElasticSearch articleForElasticSearch = currentSearchHits.getContent();
                Article article = articleForElasticSearch.toArticle();
                ArticleSummaryQueryBO resultItem = new ArticleSummaryQueryBO(article,
                        allBoardMap.get(article.getBoardNo()),
                        totalArticleCategoryMap.get(article.getArticleCategoryNo()),
                        ArticleCategoryServiceImpl.articleCategoryTreeInfo.get(article.getArticleCategoryNo()));
                resultSet.add(resultItem);
            });
            result.setResultSet(resultSet);
        } else {
            result.setResultSet(new ArrayList<>(0));
        }
        result.setTotalCount(totalCount);
        return result;
    }

    private SearchHits<ArticleForElasticSearch> queryFromElasticSearch(String keyword, ArrayList<String> allowableArticleCategoryNoList, Integer currentPage, Integer pageSize) {
        // 聚合后的最终条件构造器
        BoolQueryBuilder rootQueryBuilder = QueryBuilders.boolQuery();
        // 条件 1 文章类别必须与下列之一匹配
        BoolQueryBuilder boolQueryBuilderPart1 = QueryBuilders.boolQuery();
        for (String articleCategoryNo : allowableArticleCategoryNoList) {
            boolQueryBuilderPart1.should(QueryBuilders.termQuery("articleCategoryNo", articleCategoryNo));
        }
        rootQueryBuilder.must(boolQueryBuilderPart1);
        // 条件 2 多重字段匹配
        MultiMatchQueryBuilder multiMatchQueryBuilderPart2 = QueryBuilders
                // 指定多重匹配字段、关键字
                .multiMatchQuery(keyword, "title", "summary", "content")
                // 指定模式 _score 按 title 、summary、content 之和(默认为 BEST_FIELDS 只取 title、summary、content 中的最高分)
                .type(MultiMatchQueryBuilder.Type.MOST_FIELDS);
        rootQueryBuilder.must(multiMatchQueryBuilderPart2);

        String source = rootQueryBuilder.toString();
        StringQuery rootQuery = new StringQuery(source);
        // 设置查询结果只需返回 articleId、title
        rootQuery.addSourceFilter(ARTICLE_SUMMARY_SOURCE_FILTER);
        // 设置分页 当前页(从 0 开始为第一页) 页容量
        PageRequest pageRequest = PageRequest.of(currentPage - 1, pageSize, SCORE_DESC_SORT);
        rootQuery.setPageable(pageRequest);
        SearchHits<ArticleForElasticSearch> searchHits = elasticsearchRestTemplate.search(rootQuery, ArticleForElasticSearch.class);
        return searchHits;
    }
}