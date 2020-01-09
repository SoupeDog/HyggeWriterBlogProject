package org.xavier.blog.article.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.xavier.blog.article.dao.ArticleMapper;
import org.xavier.blog.article.domain.bo.ArticleQuarryBO;
import org.xavier.blog.article.domain.bo.ArticleSummaryQueryBO;
import org.xavier.blog.article.domain.bo.UserValidateBO;
import org.xavier.blog.article.domain.dto.ArticleDTO;
import org.xavier.blog.article.domain.po.article.Article;
import org.xavier.blog.article.domain.po.article.ArticleCategory;
import org.xavier.blog.article.domain.po.article.ArticleQuarryPO;
import org.xavier.blog.article.domain.po.article.ArticleSummaryQueryPO;
import org.xavier.blog.article.service.remote.UserServiceImpl;
import org.xavier.blog.common.ErrorCode;
import org.xavier.blog.common.PropertiesReminder;
import org.xavier.blog.common.enums.UserTypeEnum;
import org.xavier.common.enums.ColumnType;
import org.xavier.common.exception.Universal403Exception;
import org.xavier.common.exception.Universal404Exception;
import org.xavier.common.logging.HyggeLoggerMsgBuilder;
import org.xavier.common.util.UtilsCreator;
import org.xavier.common.util.bo.ColumnInfo;
import org.xavier.webtoolkit.base.DefaultUtils;
import org.xavier.webtoolkit.domain.PageResult;

import java.util.*;
import java.util.concurrent.CompletableFuture;

/**
 * 描述信息：<br/>
 * Article 操作
 *
 * @author Xavier
 * @version 1.0
 * @date 2018/4/10
 * @since Jdk 1.8
 */
@Service
public class ArticleServiceImpl extends DefaultUtils {
    @Autowired
    ArticleMapper articleMapper;
    @Autowired
    BoardServiceImpl boardService;
    @Autowired
    StatementServiceImpl statementService;
    @Autowired
    UserServiceImpl userService;
    @Autowired
    ArticleCategoryServiceImpl articleCategoryService;

    private static List<ColumnInfo> checkInfo;

    static {
        checkInfo = new ArrayList<ColumnInfo>() {{
            add(new ColumnInfo("title", "title", ColumnType.STRING, false, 1, 50));
            add(new ColumnInfo("articleCategoryId", "articleCategoryId", ColumnType.STRING, false, 1, 32));
            add(new ColumnInfo("uId", "uId", ColumnType.STRING, false, 1, 10));
            add(new ColumnInfo("statementId", "statementId", ColumnType.STRING, true, 1, 32));
            add(new ColumnInfo("summary", "summary", ColumnType.STRING, true, 1, 1000));
            add(new ColumnInfo("content", "content", ColumnType.STRING, false, 1, 100000));
        }};
    }

    /**
     * 创建文章
     *
     * @param article 目标文章
     */
    public Boolean saveArticle(Article article, Long serviceTs) throws Universal404Exception, Universal403Exception {
        article.setPageViews(0);
        article.setLegal_Flag(true);
        article.setLastUpdateTs(serviceTs);
        article.setTs(serviceTs);
        article.setArticleId(UtilsCreator.getDefaultRandomHelperInstance().getUniversallyUniqueIdentifier());
        ArticleCategory articleCategory = articleCategoryService.queryArticleCategoryById_WithExistValidate(article.getArticleCategoryId());
        if (!articleCategory.getuId().equals(article.getuId())) {
            throw new Universal403Exception(ErrorCode.INSUFFICIENT_PERMISSIONS.getErrorCod(), "Permission denied.");
        }
        if (article.getStatementId() != null && !article.getStatementId().equals("")) {
            statementService.queryStatementByStatementId_WithExistValidate(article.getStatementId());
        }
        Integer saveArticleAffectedRow = articleMapper.saveArticle(article);
        Boolean saveArticleFlag = saveArticleAffectedRow == 1;
        if (!saveArticleFlag) {
            logger.warn(HyggeLoggerMsgBuilder.assertFail("saveArticleAffectedRow", "1", saveArticleAffectedRow, article));
        }
        return saveArticleFlag;
    }

    /**
     * 批量逻辑删除文章
     *
     * @param articleIds 文章唯一标识 List
     */
    public Boolean removeArticleMultipleByIdsLogically(String operatorUId, List<String> articleIds, Long upTs) {
        ArrayList<String> articleIdListForQuery = collectionHelper.filterCollectionNotEmptyAsArrayList(true, articleIds, "articleIds", String.class, String.class, (x) -> x.trim());
        propertiesHelper.intRangeNotNull(articleIdListForQuery.size(), 1, Integer.MAX_VALUE, "[articleIds] can't be empty.");
        ArrayList<String> articleIdListForRemove = articleMapper.queryArticleIdLisOfUser(operatorUId, articleIdListForQuery);
        propertiesHelper.intRangeNotNull(articleIdListForRemove.size(), 1, Integer.MAX_VALUE, "[articleIds] can't be empty.");
        articleIdListForRemove = collectionHelper.filterCollectionNotEmptyAsArrayList(true, articleIdListForRemove, "articleIds", String.class, String.class, (x) -> x.trim());
        Integer removeArticleMultipleAffectedRow = articleMapper.removeArticleMultipleByIds_Logically(articleIdListForRemove, operatorUId, upTs);
        Boolean removeArticleMultipleFlag = removeArticleMultipleAffectedRow == articleIdListForRemove.size();
        if (!removeArticleMultipleFlag) {
            ArrayList<String> finalArticleIdListForRemove = articleIdListForRemove;
            logger.warn(HyggeLoggerMsgBuilder.assertFail("saveArticleAffectedRow", String.valueOf(articleIdListForRemove.size()), removeArticleMultipleAffectedRow, new LinkedHashMap() {{
                put("operatorUId", operatorUId);
                put("articleIdListForRemove", finalArticleIdListForRemove);
                put("upTs", upTs);
            }}));
        }
        return removeArticleMultipleFlag;
    }

    public void increasePageViewsAsynchronous(String articleId) {
        if (articleId == null || articleId.trim().equals("")) {
            return;
        }
        CompletableFuture.runAsync(() -> {
            Integer autoIncreaseArticlePageViewsAffectedRow = articleMapper.autoIncreaseArticlePageViews(articleId);
            if (autoIncreaseArticlePageViewsAffectedRow != 1) {
                logger.warn(HyggeLoggerMsgBuilder.assertFail("autoIncreaseArticlePageViewsAffectedRow", String.valueOf(1), autoIncreaseArticlePageViewsAffectedRow, articleId));
            }
        }).exceptionally(throwable -> {
            if (throwable != null) {
                logger.error(throwable.getMessage(), throwable);
            }
            return null;
        });
    }

    /**
     * 修改文章信息
     *
     * @param articleId 文章唯一标识
     * @param rawData   修改原数据
     */
    public Boolean updateArticle(String operatorUId, String articleId, Map rawData) throws Universal404Exception, Universal403Exception {
        Article targetArticle = querySingleArticleByArticleId_WithExistValidate(articleId);
        userService.checkRight(operatorUId, UserTypeEnum.ROOT, targetArticle.getuId());
        Long upTs = propertiesHelper.longRangeNotNull(rawData.get("ts"), "[ts] can't be null,and it should be a number.");

        if (rawData.containsKey("statementId")) {
            String statementId = propertiesHelper.string(rawData.get("statementId"), 32, 32, "[statementId] can't be null and its length should be 32.");
            if (statementId != null) {
                statementService.queryStatementByStatementId_WithExistValidate(statementId);
            } else {
                rawData.remove("statementId");
            }
        }
        if (rawData.containsKey("articleCategoryId")) {
            String articleCategoryId = propertiesHelper.string(rawData.get("articleCategoryId"), 32, 32, "[articleCategoryId] can't be null and its length should be 32.");
            if (articleCategoryId != null) {
                articleCategoryService.queryArticleCategoryById_WithExistValidate(articleCategoryId);
            } else {
                rawData.remove("articleCategoryId");
            }
        }
        if (rawData.containsKey("content")) {
            Integer wordCount = rawData.get("content").toString().trim().length();
            rawData.put("wordCount", wordCount);
        }
        HashMap<String, Object> data = sqlHelper.createFinalUpdateDataWithDefaultTsColumn(upTs, rawData, checkInfo);
        Integer updateArticle_affectedLine = articleMapper.updateArticle(articleId, data, upTs);
        Boolean updateArticle_Flag = updateArticle_affectedLine == 1;
        if (!updateArticle_Flag) {
            logger.warn(HyggeLoggerMsgBuilder.assertFail("updateArticle_affectedLine", String.valueOf(1), updateArticle_affectedLine, new LinkedHashMap() {{
                put("operatorUId", operatorUId);
                put("articleIdListForRemove", updateArticle_affectedLine);
                put("data", data);
                put("upTs", upTs);
            }}));
        }
        return updateArticle_Flag;
    }

    public ArticleQuarryBO queryArticleByArticleId_WithBusinessCheck(HttpHeaders headers, String operatorUId, String secretKey, String articleId) {
        propertiesHelper.stringNotNull(operatorUId, 9, 10, "[uId] can't be null,and its length should be between 9~10.");
        propertiesHelper.stringNotNull(articleId, 32, 32, "[articleId] can't be null,and its length should be 32.");
        ArticleQuarryPO articleQuarryPO = articleMapper.queryArticleQuarryBOByArticleId(articleId);
        if (articleQuarryPO == null) {
            return null;
        }
        ArticleQuarryBO result = new ArticleQuarryBO(articleQuarryPO);
        ArticleCategory articleCategoryTemp = articleCategoryService.queryArticleCategoryByArticleCategoryId(result.getArticleCategoryId());
        UserValidateBO currentUser = userService.queryUserValidateBOByUId(operatorUId, secretKey);
        if (currentUser.chekPromission(articleCategoryTemp)) {
            increasePageViewsAsynchronous(result.getArticleId());
            userService.addUserLog_Async(operatorUId,
                    PropertiesReminder.浏览文章,
                    result.getTitle(),
                    headers.getFirst(PropertiesReminder.DESC_REAL_IP_NAME),
                    headers.getFirst(PropertiesReminder.请求方代理设备信息),
                    System.currentTimeMillis()
            );
            return result;
        }
        return null;
    }

    public PageResult<ArticleSummaryQueryBO> queryArticleSummaryOfUser_WithBusinessCheck(String operatorUId, String uId, String boardId, String secretKey, Integer currentPage, Integer size, String orderKeyTemp, Boolean isDESC) {
        propertiesHelper.stringNotNull(operatorUId, 9, 10, "[uId] can't be null,and its length should be between 9~10.");
        propertiesHelper.stringNotNull(boardId, 32, 32, "[boardId] can't be null,and its length should be 32.");
        ArrayList<ArticleCategory> articleCategoryList = articleCategoryService.queryArticleCategoryByUId(operatorUId, boardId, uId, secretKey);
        ArrayList<String> articleCategoryIdList = new ArrayList();
        for (ArticleCategory temp : articleCategoryList) {
            articleCategoryIdList.add(temp.getArticleCategoryId());
        }
        PageResult<ArticleSummaryQueryBO> result = new PageResult();
        String orderKey;
        switch (orderKeyTemp) {
            case "lastUpdateTs":
                orderKey = "lastUpdateTs";
                break;
            case "pageViews":
                orderKey = "pageViews";
                break;
            default:
                orderKey = "ts";
        }
        ArrayList<ArticleSummaryQueryPO> resultSetTemp;
        if (isDESC) {
            resultSetTemp = articleMapper.queryArticleQuarryBOByArticleIdList(articleCategoryIdList, (currentPage - 1) * size, size, orderKey, "DESC");
        } else {
            resultSetTemp = articleMapper.queryArticleQuarryBOByArticleIdList(articleCategoryIdList, (currentPage - 1) * size, size, orderKey, "ASC");
        }
        ArrayList<ArticleSummaryQueryBO> resultSet = new ArrayList();
        for (ArticleSummaryQueryPO temp : resultSetTemp) {
            resultSet.add(new ArticleSummaryQueryBO(temp));
        }
        result.setTotalCount(articleMapper.queryArticleQuarryBOByArticleIdList_TotalCount(articleCategoryIdList));
        result.setResultSet(resultSet);
        return result;
    }

    /**
     * 根据文章唯一标识查询文章
     *
     * @param articleId 文章唯一标识
     * @return 文章对象
     * @throws Universal404Exception
     */
    public Article querySingleArticleByArticleId_WithExistValidate(String articleId) throws Universal404Exception {
        Article result = querySingleArticleByArticleId(articleId);
        if (result == null) {
            throw new Universal404Exception(ErrorCode.ARTICLE_NOTFOUND.getErrorCod(), "Article(" + articleId + ") was not found.");
        }
        return result;
    }

    /**
     * 根据文章唯一标识查询文章
     *
     * @param articleId 文章唯一标识
     * @return 文章对象
     */
    public Article querySingleArticleByArticleId(String articleId) {
        propertiesHelper.stringNotNull(articleId, "[articleId] can't be empty.");
        ArrayList<Article> articleList = articleMapper.queryArticleListByIds(new ArrayList<String>() {{
            add(articleId);
        }});
        if (articleList.size() > 0) {
            return articleList.get(0);
        } else {
            return null;
        }
    }

    public ArticleDTO articleToArticleDTO(Article article) {
        if (article == null) {
            return null;
        }
        return new ArticleDTO(article);
    }

    public ArrayList<ArticleDTO> articleToArticleDTO(List<Article> articleList) {
        ArrayList<ArticleDTO> result = new ArrayList();
        for (Article temp : articleList) {
            result.add(new ArticleDTO(temp));
        }
        return result;
    }
}