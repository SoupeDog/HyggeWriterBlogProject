package org.xavier.blog.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.xavier.blog.common.ErrorCode;
import org.xavier.blog.common.enums.DefaultStateEnum;
import org.xavier.blog.common.enums.UserTypeEnum;
import org.xavier.blog.dao.AccessRuleMapper;
import org.xavier.blog.dao.ArticleCategoryMapper;
import org.xavier.blog.dao.ArticleMapper;
import org.xavier.blog.dao.GroupRelationshipMapper;
import org.xavier.blog.domain.bo.*;
import org.xavier.blog.domain.dto.ArticleDTO;
import org.xavier.blog.domain.po.*;
import org.xavier.common.enums.ColumnType;
import org.xavier.common.exception.PropertiesRuntimeException;
import org.xavier.common.exception.Universal403Exception;
import org.xavier.common.exception.Universal404Exception;
import org.xavier.common.logging.HyggeLoggerMsgBuilder;
import org.xavier.common.util.JsonHelper;
import org.xavier.common.util.UtilsCreator;
import org.xavier.common.util.bo.ColumnInfo;
import org.xavier.webtoolkit.base.DefaultUtils;
import org.xavier.webtoolkit.domain.PageResult;

import java.util.*;
import java.util.concurrent.CompletableFuture;

/**
 * 描述信息：<br/>
 *
 * @author Xavier
 * @version 1.0
 * @date 19-12-29
 * @since Jdk 1.8
 */
@Service
public class ArticleServiceImpl extends DefaultUtils {
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

    private static final List<ColumnInfo> checkInfo = new ArrayList<ColumnInfo>() {{
        add(new ColumnInfo("boardNo", "boardNo", ColumnType.STRING, false, 1, 32));
        add(new ColumnInfo("articleCategoryNo", "articleCategoryNo", ColumnType.STRING, false, 1, 32));
        add(new ColumnInfo("title", "title", ColumnType.STRING, false, 1, 32));
        add(new ColumnInfo("summary", "summary", ColumnType.STRING, false, 1, 500));
        add(new ColumnInfo("content", "content", ColumnType.STRING, false, 1, 100000));
        add(new ColumnInfo("properties", "properties", ColumnType.STRING, true, 0, 100000));
    }};

    public Boolean saveArticle(Article article, Long currentTs) throws Universal404Exception {
        article.validate();
        article.setCreateTs(currentTs);
        article.setLastUpdateTs(currentTs);
        article.setPageViews(0);
        article.setArticleNo(UtilsCreator.getDefaultRandomHelperInstance().getUniversallyUniqueIdentifier());
        article.setState(DefaultStateEnum.ACTIVE);
        articleCategoryService.queryArticleCategoryNoByArticleCategoryNoNotNull(article.getArticleCategoryNo());
        boardService.queryBoardByBoardNoNotNull(article.getBoardNo());
        Integer saveArticleAffectedRow = articleMapper.saveArticle(article);
        Boolean saveArticleFlag = saveArticleAffectedRow == 1;
        if (!saveArticleFlag) {
            logger.warn(HyggeLoggerMsgBuilder.assertFail("saveArticleAffectedRow", "1", saveArticleAffectedRow, article));
        }
        return saveArticleFlag;
    }

    /**
     * 修改文章信息
     *
     * @param articleNo 文章唯一标识
     * @param rawData   修改原数据
     */
    public Boolean updateArticle(String operatorUId, String articleNo, Map rawData) throws Universal404Exception, Universal403Exception {
        Article targetArticle = querySingleArticleByArticleNoNotNull(articleNo);
        User operatorUser = userService.queryUserNotNull(operatorUId);
        userService.checkRight(operatorUser, UserTypeEnum.ROOT, targetArticle.getUid());
        Long upTs = propertiesHelper.longRangeNotNull(rawData.get("ts"), "[ts] can't be null,and it should be a number.");
        if (rawData.containsKey("boardNo")) {
            String boardNo = propertiesHelper.string(rawData.get("boardNo"), 0, 32, "[boardNo] can't be null and its length should be [0,32].");
            if (boardNo != null) {
                boardService.queryBoardByBoardNoNotNull(boardNo);
            } else {
                rawData.remove("boardNo");
            }
        }
        if (rawData.containsKey("properties")) {
            String properties = jsonHelper.format(rawData.get("properties"));
            if (properties != null) {
                JsonHelper<ObjectMapper> jsonHelper = UtilsCreator.getDefaultJsonHelperInstance(false);
                ArticleJsonProperties articleJsonProperties = null;
                try {
                    articleJsonProperties = jsonHelper.getDependence().readValue(properties, ArticleJsonProperties.class);
                } catch (JsonProcessingException e) {
                    throw new PropertiesRuntimeException("Fail to read [properties]:" + properties);
                }
                properties = jsonHelper.format(articleJsonProperties);
                rawData.put("properties", properties);
            } else {
                rawData.remove("properties");
            }
        }
        if (rawData.containsKey("articleCategoryNo")) {
            String articleCategoryNo = propertiesHelper.string(rawData.get("articleCategoryNo"), 0, 32, "[articleCategoryNo] can't be null and its length should be [0,32].");
            if (articleCategoryNo != null) {
                articleCategoryService.queryArticleCategoryNoByArticleCategoryNoNotNull(articleCategoryNo);
            } else {
                rawData.remove("articleCategoryNo");
            }
        }
        if (rawData.containsKey("content")) {
            Integer wordCount = rawData.get("content").toString().trim().length();
            rawData.put("wordCount", wordCount);
        }
        HashMap<String, Object> data = sqlHelper.createFinalUpdateDataWithDefaultTsColumn(upTs, rawData, checkInfo);
        Integer updateArticleAffectedRow = articleMapper.updateArticleByArticleNo(articleNo, data, upTs);
        Boolean updateArticleFlag = updateArticleAffectedRow == 1;
        if (!updateArticleFlag) {
            logger.warn(HyggeLoggerMsgBuilder.assertFail("updateArticleAffectedRow", String.valueOf(1), updateArticleAffectedRow, new LinkedHashMap() {{
                put("operatorUId", operatorUId);
                put("articleIdListForRemove", updateArticleAffectedRow);
                put("data", data);
                put("upTs", upTs);
            }}));
        }
        return updateArticleFlag;
    }

    public void increasePageViewsAsynchronous(String articleNo) {
        if (articleNo == null || "".equals(articleNo.trim())) {
            return;
        }
        CompletableFuture.runAsync(() -> {
            Integer autoIncreaseArticlePageViewsAffectedRow = articleMapper.increaseArticlePageViewsByArticleNo(articleNo, 1);
            if (autoIncreaseArticlePageViewsAffectedRow != 1) {
                logger.warn(HyggeLoggerMsgBuilder.assertFail("autoIncreaseArticlePageViewsAffectedRow", String.valueOf(1), autoIncreaseArticlePageViewsAffectedRow, articleNo));
            }
        }).exceptionally(throwable -> {
            if (throwable != null) {
                logger.error(throwable.getMessage(), throwable);
            }
            return null;
        });
    }

    /**
     * 根据文章唯一标识查询文章
     *
     * @param articleNo 文章唯一标识
     * @return 文章对象
     */
    public ArticleDTO querySingleArticleByArticleNoForUser(String loginUid, String articleNo, String secretKey) throws Universal404Exception {
        User loginUser = userService.queryUserNotNull(loginUid);
        Article article = querySingleArticleByArticleNoNotNull(articleNo);
        ArrayList<AccessRule> accessRuleList = accessRuleMapper.queryAccessRuleByArticleCategoryNo(article.getArticleCategoryNo());
        ArrayList<String> allJoinedGroupTemp = groupRelationshipMapper.queryGroupIdListOfUser(loginUid);
        HashMap<String, Object> allJoinedGroup = collectionHelper.filterCollectionNotEmptyAsHashMap(allJoinedGroupTemp, "Item of [gno] can't be null.",
                (gno) -> gno,
                (gno) -> gno);
        if (!checkAccessRuleOfOneArticleCategoryNo(secretKey, loginUser, accessRuleList, allJoinedGroup)) {
            throw new Universal404Exception(ErrorCode.ARTICLE_NOTFOUND.getErrorCod(), "Article(" + articleNo + ") was not found.");
        }
        ArticleCategory articleCategory = articleCategoryService.queryArticleCategoryNoByArticleCategoryNoNotNull(article.getArticleCategoryNo());
        Board board = boardService.queryBoardByBoardNoNotNull(article.getBoardNo());
        ArticleDTO result = new ArticleDTO(article, board, articleCategory, ArticleCategoryServiceImpl.articleCategoryTreeInfo.get(article.getArticleCategoryNo()));
        increasePageViewsAsynchronous(articleNo);
        return result;
    }

    private boolean checkAccessRuleOfOneArticleCategoryNo(String secretKey, User loginUser, List<AccessRule> accessRuleList, HashMap<String, Object> allJoinedGroup) {
        boolean pass = false;
        for (AccessRule accessRule : accessRuleList) {
            if (accessRule.getRequirement()) {
                pass = pass && checkAccessRule(loginUser, accessRule, allJoinedGroup, secretKey);
                if (!pass) {
                    break;
                }
            } else {
                pass = checkAccessRule(loginUser, accessRule, allJoinedGroup, secretKey) || pass;
            }
        }
        return pass;
    }

    private boolean checkAccessRule(User loginUser, AccessRule accessRule, HashMap<String, Object> allJoinedGroup, String secretKey) {
        switch (accessRule.getAccessPermit()) {
            case PUBLIC:
                return true;
            case PERSONAL:
                return loginUser.getUid().equals(accessRule.getExtendProperties());
            case SECRET_KEY:
                return accessRule.getExtendProperties().equals(secretKey);
            case GROUP:
                return allJoinedGroup.containsKey(accessRule.getExtendProperties());
            default:
                return false;
        }
    }


    /**
     * 根据文章唯一标识查询文章
     *
     * @param articleNo 文章唯一标识
     * @return 文章对象
     */
    public Article querySingleArticleByArticleNo(String articleNo) {
        propertiesHelper.stringNotNull(articleNo, "[articleNo] can't be empty.");
        Article article = articleMapper.queryArticleByArticleNo(articleNo);
        return article;
    }

    /**
     * 根据文章唯一标识查询文章
     *
     * @param articleNo 文章唯一标识
     * @return 文章对象
     */
    public Article querySingleArticleByArticleNoNotNull(String articleNo) throws Universal404Exception {
        Article article = querySingleArticleByArticleNo(articleNo);
        if (article == null) {
            throw new Universal404Exception(ErrorCode.ARTICLE_NOTFOUND.getErrorCod(), "Article(" + articleNo + ") was not found.");
        }
        return article;
    }

    public PageResult<ArticleSummaryQueryBO> queryArticleSummaryOfBoard(String loginUid, String boardNo, String secretKey, Integer currentPage, Integer pageSize, String orderKey, Boolean isDESC) throws Universal404Exception {
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
        Integer totalCount = articleMapper.queryArticleSummaryByArticleCategoryNoTotalCount(allowableArticleCategoryNoList, boardNo);
        if (totalCount != 0) {
            switch (orderKey) {
                case "wordCount":
                    orderKey = "wordCount";
                    break;
                case "pageViews":
                    orderKey = "pageViews";
                    break;
                case "lastUpdateTs":
                    orderKey = "lastUpdateTs";
                    break;
                default:
                    orderKey = "createTs";
            }
            Board board = boardService.queryBoardByBoardNo(boardNo);
            ArrayList<Article> resultSetTemp = articleMapper.queryArticleSummaryByArticleCategoryNo(allowableArticleCategoryNoList, boardNo, (currentPage - 1) * pageSize, pageSize, orderKey, isDESC ? "DESC" : "ASC");
            ArrayList<ArticleSummaryQueryBO> resultSet = collectionHelper.filterCollectionNotEmptyAsArrayList(true, resultSetTemp, "",
                    (article) -> new ArticleSummaryQueryBO(article,
                            board,
                            totalArticleCategoryMap.get(article.getArticleCategoryNo()),
                            ArticleCategoryServiceImpl.articleCategoryTreeInfo.get(article.getArticleCategoryNo())));
            result.setResultSet(resultSet);
        } else {
            result.setResultSet(new ArrayList<>(0));
        }
        result.setTotalCount(totalCount);
        return result;
    }

    public PageResult<ArticleSummaryQueryBO> queryArticleSummaryOfArticleCategory(String loginUid, String articleCategoryNo, String secretKey, Integer currentPage, Integer pageSize, String orderKey, Boolean isDESC) throws Universal404Exception {
        ArticleCategory articleCategory = articleCategoryService.queryArticleCategoryNoByArticleCategoryNoNotNull(articleCategoryNo);
        PageResult<ArticleSummaryQueryBO> result = new PageResult<>();
        User loginUser = userService.queryUserNotNull(loginUid);
        ArrayList<String> allJoinedGroupTemp = groupRelationshipMapper.queryGroupIdListOfUser(loginUid);
        HashMap<String, Object> allJoinedGroup = collectionHelper.filterCollectionNotEmptyAsHashMap(allJoinedGroupTemp, "Item of [gno] can't be null.",
                (gno) -> gno,
                (gno) -> gno);
        // key-value articleCategoryNo-ArticleCategory
        HashMap<String, ArticleCategory> totalArticleCategoryMap = new HashMap() {{
            put(articleCategory.getArticleCategoryNo(), articleCategory);
        }};

        ArrayList<String> needCheckArticleCategoryNoList = new ArrayList() {{
            add(articleCategory.getArticleCategoryNo());
        }};

        String boardNo = articleCategory.getBoardNo();
        ArrayList<AccessRule> accessRuleList = accessRuleMapper.queryAccessRuleByArticleCategoryNoMultiple(needCheckArticleCategoryNoList);
        ArrayList<String> allowableArticleCategoryNoList = getAllowableArticleCategoryNoList(secretKey, loginUser, allJoinedGroup, accessRuleList);
        Integer totalCount = allowableArticleCategoryNoList.size() < 1 ? 0 : articleMapper.queryArticleSummaryByArticleCategoryNoTotalCount(allowableArticleCategoryNoList, boardNo);
        if (totalCount != 0) {
            switch (orderKey) {
                case "wordCount":
                    orderKey = "wordCount";
                    break;
                case "pageViews":
                    orderKey = "pageViews";
                    break;
                case "lastUpdateTs":
                    orderKey = "lastUpdateTs";
                    break;
                default:
                    orderKey = "createTs";
            }
            Board board = boardService.queryBoardByBoardNo(boardNo);
            ArrayList<Article> resultSetTemp = articleMapper.queryArticleSummaryByArticleCategoryNo(allowableArticleCategoryNoList, boardNo, (currentPage - 1) * pageSize, pageSize, orderKey, isDESC ? "DESC" : "ASC");
            ArrayList<ArticleSummaryQueryBO> resultSet = collectionHelper.filterCollectionNotEmptyAsArrayList(true, resultSetTemp, "",
                    (article) -> new ArticleSummaryQueryBO(article,
                            board,
                            totalArticleCategoryMap.get(article.getArticleCategoryNo()),
                            ArticleCategoryServiceImpl.articleCategoryTreeInfo.get(article.getArticleCategoryNo())));
            result.setResultSet(resultSet);
        } else {
            result.setResultSet(new ArrayList<>(0));
        }
        result.setTotalCount(totalCount);
        return result;
    }

    /**
     * 文章全文检索(从 title summary content 匹配)
     */
    public PageResult<ArticleSummaryQueryBO> articleSearch(String loginUid, String secretKey, String keyWord, Integer currentPage, Integer pageSize) throws Universal404Exception {
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
        Integer totalCount = articleMapper.queryArticleNoCountForSearch(keyWord, allowableArticleCategoryNoList);
        if (totalCount != 0) {
            ArrayList<Board> allBoardList = boardService.queryAllBoardList(1, 999, "ts", false);
            HashMap<String, Board> allBoardMap = collectionHelper.filterCollectionNotEmptyAsHashMap(allBoardList, "", (board) -> board.getBoardNo(), (board -> board));
            ArrayList<String> finalArticleNoList = articleMapper.queryArticleNoForSearch(keyWord, allowableArticleCategoryNoList, (currentPage - 1) * pageSize, pageSize);
            ArrayList<Article> resultSetTemp = articleMapper.queryArticleByArticleNoList(finalArticleNoList);
            ArrayList<ArticleSummaryQueryBO> resultSet = collectionHelper.filterCollectionNotEmptyAsArrayList(true, resultSetTemp, "",
                    (article) -> new ArticleSummaryQueryBO(article,
                            allBoardMap.get(article.getBoardNo()),
                            totalArticleCategoryMap.get(article.getArticleCategoryNo()),
                            ArticleCategoryServiceImpl.articleCategoryTreeInfo.get(article.getArticleCategoryNo())));
            result.setResultSet(resultSet);
        } else {
            result.setResultSet(new ArrayList<>(0));
        }
        result.setTotalCount(totalCount);
        return result;
    }

    /**
     * 查询全部板块文章数量
     *
     * @return 全部板块文章数量信息 key-value BoardNo-BoardArticleCountInfo
     */
    public LinkedHashMap<String, BoardArticleCountInfo> queryArticleCountInfoOfBoard(String loginUid, String secretKey) throws Universal404Exception {
        ArrayList<Board> allBoardList = boardService.queryAllBoardList(1, 999, "ts", false);
        LinkedHashMap<String, BoardArticleCountInfo> result = new LinkedHashMap(allBoardList.size());

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
        ArrayList<AccessRule> accessRuleList = accessRuleMapper.queryAccessRuleByArticleCategoryNoMultiple(needCheckArticleCategoryNoList);
        ArrayList<String> allowableArticleCategoryNoList = getAllowableArticleCategoryNoList(secretKey, loginUser, allJoinedGroup, accessRuleList);

        for (Board board : allBoardList) {
            BoardArticleCountInfo boardArticleCountInfo = new BoardArticleCountInfo(board);
            if (needCheckArticleCategoryNoList.size() < 1) {
                boardArticleCountInfo.setTotalCount(0);
            } else {
                Integer totalCount = articleMapper.queryArticleSummaryByArticleCategoryNoTotalCount(allowableArticleCategoryNoList, board.getBoardNo());
                boardArticleCountInfo.setTotalCount(totalCount);
            }
            result.put(board.getBoardNo(), boardArticleCountInfo);
        }
        return result;
    }

    /**
     * 查询全部板块文章数量
     *
     * @return 全部板块文章数量信息
     */
    public ArrayList<BoardArticleCategoryArticleCountInfo> queryArticleCountInfoOfArticleCategory(String loginUid, String secretKey) throws Universal404Exception {
        ArrayList<ArticleCategory> totalArticleCategoryList = articleCategoryService.queryAllArticleCategory(null);
        ArrayList<BoardArticleCategoryArticleCountInfo> result;


        User loginUser = userService.queryUserNotNull(loginUid);
        ArrayList<String> allJoinedGroupTemp = groupRelationshipMapper.queryGroupIdListOfUser(loginUid);
        HashMap<String, Object> allJoinedGroup = collectionHelper.filterCollectionNotEmptyAsHashMap(allJoinedGroupTemp, "Item of [gno] can't be null.",
                (gno) -> gno,
                (gno) -> gno);

        // key-value articleCategoryNo-ArticleCategory
        HashMap<String, ArticleCategory> totalArticleCategoryMap = collectionHelper.filterCollectionNotEmptyAsHashMap(totalArticleCategoryList, "",
                (articleCategory) -> articleCategory.getArticleCategoryNo(),
                (articleCategory) -> articleCategory);
        ArrayList<String> needCheckArticleCategoryNoList = collectionHelper.filterCollectionNotEmptyAsArrayList(true, totalArticleCategoryList, "[totalArticleCategoryList] for query can't be empty.",
                (articleCategory) -> articleCategory.getArticleCategoryNo());

        ArrayList<AccessRule> accessRuleList = accessRuleMapper.queryAccessRuleByArticleCategoryNoMultiple(needCheckArticleCategoryNoList);
        ArrayList<String> allowableArticleCategoryNoList = getAllowableArticleCategoryNoList(secretKey, loginUser, allJoinedGroup, accessRuleList);

        ArrayList<Board> allBoardList = boardService.queryAllBoardList(1, 999, "ts", false);
        // 全部板块信息 key-vale boardNo-Board
        HashMap<String, Board> allBoardMap = collectionHelper.filterCollectionNotEmptyAsHashMap(allBoardList, "Board can't be null.", (board) -> board.getBoardNo(), board -> board);


        LinkedHashMap<String, BoardArticleCategoryArticleCountInfo> resultTemp = new LinkedHashMap();
        for (Map.Entry<String, ArticleCategory> entry : totalArticleCategoryMap.entrySet()) {
            if (allowableArticleCategoryNoList.contains(entry.getKey())) {
                ArticleCategoryArticleCountInfo articleCategoryArticleCountInfo = new ArticleCategoryArticleCountInfo(entry.getValue());
                if (needCheckArticleCategoryNoList.size() < 1) {
                    articleCategoryArticleCountInfo.setTotalCount(0);
                } else {
                    Integer totalCount = articleMapper.queryArticleCategoryArticleCount(entry.getKey());
                    articleCategoryArticleCountInfo.setTotalCount(totalCount);
                }
                // 当前文章类别对应的板块
                Board currentBoard = allBoardMap.get(entry.getValue().getBoardNo());
                // 文章类别对应的板块信息存在
                if (currentBoard != null) {
                    if (resultTemp.containsKey(currentBoard.getBoardNo())) {
                        resultTemp.get(currentBoard.getBoardNo()).getArticleCategoryCountInfoMap().put(entry.getKey(), articleCategoryArticleCountInfo);
                    } else {
                        BoardArticleCategoryArticleCountInfo boardArticleCategoryArticleCountInfo = new BoardArticleCategoryArticleCountInfo();
                        boardArticleCategoryArticleCountInfo.setBoard(currentBoard);
                        boardArticleCategoryArticleCountInfo.getArticleCategoryCountInfoMap().put(entry.getKey(), articleCategoryArticleCountInfo);
                        resultTemp.put(currentBoard.getBoardNo(), boardArticleCategoryArticleCountInfo);
                    }
                }
            }
        }
        result = collectionHelper.filterCollectionNotEmptyAsArrayList(true, resultTemp.values(), "Fail to get article count info of board.", (item) -> item);
        return result;
    }


    private ArrayList<String> getAllowableArticleCategoryNoList(String secretKey, User loginUser, HashMap<String, Object> allJoinedGroup, ArrayList<AccessRule> accessRuleList) {
        // key-value ArticleCategoryNo-AccessRule
        LinkedMultiValueMap<String, AccessRule> accessRuleOrderMap = new LinkedMultiValueMap<>();
        for (AccessRule accessRule : accessRuleList) {
            accessRuleOrderMap.add(accessRule.getArticleCategoryNo(), accessRule);
        }
        ArrayList<String> allowableArticleCategoryNoList = new ArrayList<>();
        for (Map.Entry<String, List<AccessRule>> entry : accessRuleOrderMap.entrySet()) {
            if (checkAccessRuleOfOneArticleCategoryNo(secretKey, loginUser, entry.getValue(), allJoinedGroup)) {
                allowableArticleCategoryNoList.add(entry.getKey());
            }
        }
        return allowableArticleCategoryNoList;
    }
}