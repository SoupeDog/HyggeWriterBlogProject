package org.xavier.blog.article.service;

import org.springframework.beans.factory.annotation.Autowired;
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
import org.xavier.common.enums.ColumnType;
import org.xavier.common.exception.Universal_403_X_Exception;
import org.xavier.common.exception.Universal_404_X_Exception;
import org.xavier.common.logging.HyggeLoggerMsgBuilder;
import org.xavier.common.utils.UtilsCreator;
import org.xavier.common.utils.bo.ColumnInfo;
import org.xavier.web.extend.DefaultService;
import org.xavier.web.extend.PageResult;

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
public class ArticleServiceImpl extends DefaultService {
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
            add(new ColumnInfo(ColumnType.STRING, "title", "title", false, 1, 50));
            add(new ColumnInfo(ColumnType.STRING, "articleCategoryId", "articleCategoryId", false, 1, 32));
            add(new ColumnInfo(ColumnType.STRING, "uId", "uId", false, 1, 10));
            add(new ColumnInfo(ColumnType.STRING, "statementId", "statementId", true, 1, 32));
            add(new ColumnInfo(ColumnType.STRING, "summary", "summary", true, 1, 1000));
            add(new ColumnInfo(ColumnType.STRING, "content", "content", false, 1, 100000));
        }};
    }

    /**
     * 创建文章
     *
     * @param article 目标文章
     */
    public Boolean saveArticle(Article article, Long serviceTs) throws Universal_404_X_Exception, Universal_403_X_Exception {
        article.setPageViews(0);
        article.setLegal_Flag(true);
        article.setLastUpdateTs(serviceTs);
        article.setTs(serviceTs);
        article.setArticleId(UtilsCreator.getInstance_DefaultRandomHelper().getUUID());
        ArticleCategory articleCategory = articleCategoryService.queryArticleCategoryById_WithExistValidate(article.getArticleCategoryId());
        if (!articleCategory.getuId().equals(article.getuId())) {
            throw new Universal_403_X_Exception(ErrorCode.INSUFFICIENT_PERMISSIONS.getErrorCod(), "Permission denied.");
        }
        if (article.getStatementId() != null && !article.getStatementId().equals("")) {
            statementService.queryStatementByStatementId_WithExistValidate(article.getStatementId());
        }
        Integer saveArticle_affectedLine = articleMapper.saveArticle(article);
        Boolean saveArticle_Flag = saveArticle_affectedLine == 1;
        if (!saveArticle_Flag) {
            logger.warn(HyggeLoggerMsgBuilder.assertFail("saveArticle_affectedLine", "1", saveArticle_affectedLine, article));
        }
        return saveArticle_Flag;
    }

    /**
     * 批量逻辑删除文章
     *
     * @param articleIds 文章唯一标识 List
     */
    public Boolean removeArticleMultipleByIds_Logically(String operatorUId, List<String> articleIds, Long upTs) {
        ArrayList<String> articleIdListForQuery = listHelper.filterStringListNotEmpty(articleIds, "articleId", 32, 32);
        ArrayList<String> articleIdListForRemove = articleMapper.queryArticleIdLisOfUser(operatorUId, articleIdListForQuery);
        articleIdListForRemove = listHelper.filterStringListNotEmpty(articleIdListForRemove, "articleId", 32, 32);
        Integer removeArticleMultiple_affectedLine = articleMapper.removeArticleMultipleByIds_Logically(articleIdListForRemove, operatorUId, upTs);
        Boolean removeArticleMultiple_Flag = removeArticleMultiple_affectedLine == articleIdListForRemove.size();
        if (!removeArticleMultiple_Flag) {
            ArrayList<String> finalArticleIdListForRemove = articleIdListForRemove;
            logger.warn(HyggeLoggerMsgBuilder.assertFail("saveArticle_affectedLine", String.valueOf(articleIdListForRemove.size()), removeArticleMultiple_affectedLine, new LinkedHashMap() {{
                put("operatorUId", operatorUId);
                put("articleIdListForRemove", finalArticleIdListForRemove);
                put("upTs", upTs);
            }}));
        }
        return removeArticleMultiple_Flag;
    }

    public void increasePageViews_Asynchronous(String articleId) {
        if (articleId == null || articleId.trim().equals("")) {
            return;
        }
        CompletableFuture.runAsync(() -> {
            Integer autoIncreaseArticlePageViews_affectedLine = articleMapper.autoIncreaseArticlePageViews(articleId);
            if (autoIncreaseArticlePageViews_affectedLine != 1) {
                logger.warn(HyggeLoggerMsgBuilder.assertFail("autoIncreaseArticlePageViews_affectedLine", String.valueOf(1), autoIncreaseArticlePageViews_affectedLine, articleId));
            }
        });
    }

    /**
     * 修改文章信息
     *
     * @param articleId 文章唯一标识
     * @param rowData   修改原数据
     */
    public Boolean updateArticle(String operatorUId, String articleId, Map rowData, Long upTs) throws Universal_404_X_Exception, Universal_403_X_Exception {
        Article targetArticle = querySingleArticleByArticleId_WithExistValidate(articleId);
        userService.checkRight(operatorUId, targetArticle.getuId());
        HashMap<String, Object> data = sqlHelper.createFinalUpdateDataWithTimeStamp(rowData, checkInfo, LASTUPDATETS);
        mapHelper.mapNotEmpty(data, "Effective Update-Info was null.");
        if (data.containsKey("statementId")) {
            String statementId = propertiesHelper.string(data.get("statementId"), 32, 32, "[statementId] can't be null and its length should be 32.");
            if (statementId != null) {
                statementService.queryStatementByStatementId_WithExistValidate(statementId);
            } else {
                data.remove("statementId");
            }
        }
        if (data.containsKey("articleCategoryId")) {
            String articleCategoryId = propertiesHelper.string(data.get("articleCategoryId"), 32, 32, "[articleCategoryId] can't be null and its length should be 32.");
            if (articleCategoryId != null) {
                articleCategoryService.queryArticleCategoryById_WithExistValidate(articleCategoryId);
            } else {
                data.remove("articleCategoryId");
            }
        }
        if (data.containsKey("content")) {
            Integer wordCount = data.get("content").toString().trim().length();
            data.put("wordCount", wordCount);
        }
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

    public ArticleQuarryBO queryArticleByArticleId_WithBusinessCheck(String operatorUId, String secretKey, String articleId) {
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
            increasePageViews_Asynchronous(result.getArticleId());
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
            resultSetTemp = articleMapper.queryArticleQuarryBOByArticleIdList(articleCategoryIdList, (currentPage - 1) * size, size, orderKey, DESC);
        } else {
            resultSetTemp = articleMapper.queryArticleQuarryBOByArticleIdList(articleCategoryIdList, (currentPage - 1) * size, size, orderKey, ASC);
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
     * @throws Universal_404_X_Exception
     */
    public Article querySingleArticleByArticleId_WithExistValidate(String articleId) throws Universal_404_X_Exception {
        Article result = querySingleArticleByArticleId(articleId);
        if (result == null) {
            throw new Universal_404_X_Exception(ErrorCode.ARTICLE_NOTFOUND.getErrorCod(), "Article(" + articleId + ") was not found.");
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
        ArrayList<Article> articleList = articleMapper.queryArticleListByIds(listHelper.createSingleList(articleId));
        if (articleList.size() > 0) {
            return articleList.get(0);
        } else {
            return null;
        }
    }

    public static ArticleDTO articleToArticleDTO(Article article) {
        if (article == null) {
            return null;
        }
        return new ArticleDTO(article);
    }

    public static ArrayList<ArticleDTO> articleToArticleDTO(List<Article> articleList) {
        ArrayList<ArticleDTO> result = new ArrayList();
        for (Article temp : articleList) {
            result.add(new ArticleDTO(temp));
        }
        return result;
    }
}