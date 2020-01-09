package org.xavier.blog.article.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.xavier.blog.article.dao.ArticleCategoryMapper;
import org.xavier.blog.article.domain.bo.UserValidateBO;
import org.xavier.blog.article.domain.dto.ArticleCategoryDTO;
import org.xavier.blog.article.domain.po.article.ArticleCategory;
import org.xavier.blog.article.domain.po.board.Board;
import org.xavier.blog.article.service.remote.UserServiceImpl;
import org.xavier.blog.common.ErrorCode;
import org.xavier.blog.common.enums.ArticleAccessPermitEnum;
import org.xavier.blog.common.enums.UserSexEnum;
import org.xavier.blog.common.enums.UserTypeEnum;
import org.xavier.common.enums.ColumnType;
import org.xavier.common.exception.Universal400Exception;
import org.xavier.common.exception.Universal403Exception;
import org.xavier.common.exception.Universal404Exception;
import org.xavier.common.logging.HyggeLoggerMsgBuilder;
import org.xavier.common.util.UtilsCreator;
import org.xavier.common.util.bo.ColumnInfo;
import org.xavier.webtoolkit.base.DefaultUtils;

import java.util.*;


/**
 * 描述信息：<br/>
 * 文章类别 Service 实现类
 *
 * @author Xavier
 * @version 1.0
 * @date 2018/4/3
 * @since Jdk 1.8
 */
@Service
public class ArticleCategoryServiceImpl extends DefaultUtils {
    @Autowired
    ArticleCategoryMapper articleCategoryMapper;
    @Autowired
    UserServiceImpl userService;
    @Autowired
    BoardServiceImpl boardService;

    private static List<ColumnInfo> checkInfo;

    static {
        checkInfo = new ArrayList<ColumnInfo>() {{
            add(new ColumnInfo("boardId", "boardId", ColumnType.STRING, false, 1, 32));
            add(new ColumnInfo("articleCategoryName", "articleCategoryName", ColumnType.STRING, false, 1, 30));
            add(new ColumnInfo("description", "description", ColumnType.STRING, true, 0, 2000));
            add(new ColumnInfo("uId", "uId", ColumnType.STRING, false, 1, 10));
            add(new ColumnInfo("accessPermit", "accessPermit", ColumnType.BYTE, false, 0, 6));
        }};
    }

    /**
     * 创建文章类别
     *
     * @param articleCategory 文章类别实体
     * @param currentTs       瞬时时间戳
     */
    public Boolean saveArticleCategory(ArticleCategory articleCategory, Long currentTs) throws Universal403Exception, Universal404Exception {
        articleCategory.setLegal_Flag(true);
        articleCategory.setTs(currentTs);
        articleCategory.setLastUpdateTs(currentTs);
        articleCategory.setArticleCategoryId(UtilsCreator.getDefaultRandomHelperInstance().getUniversallyUniqueIdentifier());
        switch (articleCategory.getAccessPermit()) {
            case PERSONAL:
                articleCategory.setExtendProperties(articleCategory.getuId());
                break;
            case SECRET_KEY:
                articleCategory.setExtendProperties(UtilsCreator.getDefaultRandomHelperInstance().getUniversallyUniqueIdentifier());
                break;
            case GROUP:
                propertiesHelper.stringNotNull(articleCategory.getExtendProperties(), "[extendProperties] can't be null.");
                break;
            default:
                articleCategory.setExtendProperties("");
                break;
        }
        Board board = boardService.quarryBoardByBoardId__WithExistValidate(articleCategory.getBoardId());
        articleCategory.setBoardName(board.getBoardName());
        Integer saveArticleCategoryAffectedRow = articleCategoryMapper.saveArticleCategory(articleCategory);
        Boolean saveArticleCategoryFlag = saveArticleCategoryAffectedRow == 1;
        if (!saveArticleCategoryFlag) {
            logger.warn(HyggeLoggerMsgBuilder.assertFail("saveArticleCategoryAffectedRow", "1", saveArticleCategoryAffectedRow, board));
        }
        return saveArticleCategoryFlag;
    }

    /**
     * 根据 用户唯一标识、文章类别唯一标识 逻辑删除类别
     *
     * @param articleCategoryIdList 文章类别唯一标识 List
     * @param upTs                  瞬时时间戳
     */
    public Boolean removeArticleCategoryMultipleByIds_Logically(String operatorUId, ArrayList<String> articleCategoryIdList, Long upTs) {
        ArrayList<String> articleCategoryIdListForQuery = collectionHelper.filterCollectionNotEmptyAsArrayList(true, articleCategoryIdList, "[articleCategoryIdList] for query can't be empty.", String.class, String.class, (x) -> x.trim());
        articleCategoryIdList = articleCategoryMapper.queryArticleCategoryIdOfUser(operatorUId, articleCategoryIdListForQuery);
        articleCategoryIdListForQuery = collectionHelper.filterCollectionNotEmptyAsArrayList(true, articleCategoryIdList, "[articleCategoryIdList] can't be empty.", String.class, String.class, (x) -> x.trim());
        Integer removeArticleCategoryMultipleAffectedRow = articleCategoryMapper.removeArticleCategoryMultipleByArticleCategoryId_Logically(operatorUId, articleCategoryIdListForQuery, upTs);
        Boolean removeArticleCategoryMultipleFlag = removeArticleCategoryMultipleAffectedRow == articleCategoryIdListForQuery.size();
        if (!removeArticleCategoryMultipleFlag) {
            ArrayList<String> finalArticleCategoryIdListForQuery = articleCategoryIdListForQuery;
            logger.warn(HyggeLoggerMsgBuilder.assertFail("removeArticleCategoryMultipleAffectedRow", "1", removeArticleCategoryMultipleAffectedRow, new LinkedHashMap() {{
                put("operatorUId", operatorUId);
                put("articleCategoryIdList", finalArticleCategoryIdListForQuery);
                put("upTs", upTs);
            }}));
        }
        return removeArticleCategoryMultipleFlag;
    }


    /**
     * 根据 类别唯一标识 更新类别
     *
     * @param articleCategoryId 类别唯一表示
     * @param rawData           更新数据
     */
    public Boolean updateArticleCategory(String operatorUId, String articleCategoryId, Map rawData, Long upTs) throws Universal403Exception, Universal404Exception, Universal400Exception {
        HashMap data = sqlHelper.createFinalUpdateDataWithDefaultTsColumn(upTs, rawData, checkInfo);
        if (data.size() < 2) {
            throw new Universal400Exception(ErrorCode.UPDATE_DATA_EMPTY.getErrorCod(), "Effective-Update-Properties can't be empty.");
        }
        ArticleCategory articleCategory = queryArticleCategoryById_WithExistValidate(articleCategoryId);
        userService.checkRight(operatorUId, UserTypeEnum.ROOT, articleCategory.getuId());
        if (rawData.containsKey("boardId")) {
            String boardId = propertiesHelper.string(data.get("boardId"));
            boardService.quarryBoardByBoardId__WithExistValidate(boardId);
        }
        Integer updateArticleCategory_affectedLine = articleCategoryMapper.updateArticleCategory(articleCategoryId, data, upTs);
        Boolean updateArticleCategory_Flag = updateArticleCategory_affectedLine == 1;
        if (!updateArticleCategory_Flag) {
            logger.warn(HyggeLoggerMsgBuilder.assertFail("updateArticleCategory_affectedLine", "1", updateArticleCategory_affectedLine, new LinkedHashMap() {{
                put("operatorUId", operatorUId);
                put("articleCategoryId", articleCategoryId);
                put("data", data);
                put("upTs", upTs);
            }}));
        }
        return updateArticleCategory_Flag;
    }

    /**
     * 返回当前用户在目标用户名下的可见类别
     *
     * @param operatorUId 当前用户唯一标识
     * @param uId         目标用户唯一标识
     */
    public ArrayList<ArticleCategory> queryArticleCategoryByUId(String operatorUId, String boardId, String uId, String secretKey) {
        propertiesHelper.stringNotNull(boardId, 32, 32, "[boardId] can't be null,and its length should be 32.");
        UserValidateBO currentUserInfo = userService.queryUserValidateBOByUId(operatorUId, secretKey);
        ArrayList<ArticleCategory> result = new ArrayList();
        ArrayList<Byte> accessPermitRange = new ArrayList();
        // 公共
        accessPermitRange.add(ArticleAccessPermitEnum.PUBLIC.getIndex());
        accessPermitRange.add(ArticleAccessPermitEnum.SECRET_KEY.getIndex());
        accessPermitRange.add(ArticleAccessPermitEnum.GROUP.getIndex());
        // 若为本人
        if (uId.equals(currentUserInfo.getUser().getuId()) || UserTypeEnum.ROOT.equals(currentUserInfo.getUser().getUserType())) {
            accessPermitRange.add(ArticleAccessPermitEnum.PERSONAL.getIndex());
            accessPermitRange.add(ArticleAccessPermitEnum.MALE.getIndex());
            accessPermitRange.add(ArticleAccessPermitEnum.FEMALE.getIndex());
            accessPermitRange.add(ArticleAccessPermitEnum.CRON.getIndex());
        } else {// 非本人
            // 性别可见
            switch (UserSexEnum.getUserSexEnum(currentUserInfo.getUser().getSex())) {
                case MALE:
                    accessPermitRange.add(ArticleAccessPermitEnum.MALE.getIndex());
                    break;
                case FEMALE:
                    accessPermitRange.add(ArticleAccessPermitEnum.FEMALE.getIndex());
                    break;
                default:
                    accessPermitRange.add(ArticleAccessPermitEnum.MALE.getIndex());
            }
        }
        ArrayList<ArticleCategory> resultTemp = articleCategoryMapper.queryArticleCategoryByUId(boardId, uId, accessPermitRange);
        for (ArticleCategory category : resultTemp) {
            if (currentUserInfo.chekPromission(category)) {
                result.add(category);
            }
        }
        return result;
    }

    /**
     * 校验了文章列表非空性的根据 articleCategoryId 查询对象(用于 Service 间调用)
     */
    public ArticleCategory queryArticleCategoryById_WithExistValidate(String articleCategoryId) throws Universal404Exception {
        ArticleCategory targetArticleCategory = queryArticleCategoryByArticleCategoryId(articleCategoryId);
        if (targetArticleCategory == null) {
            throw new Universal404Exception(ErrorCode.ARTICLECATEGORY_NOTFOUND.getErrorCod(), "ArticleCategory(" + articleCategoryId + ") was not found.");
        }
        return targetArticleCategory;
    }

    /**
     * 根据 articleCategoryId 查询对象
     */
    public ArticleCategory queryArticleCategoryByArticleCategoryId(String articleCategoryId) {
        ArrayList<ArticleCategory> targetArticleCategoryList = articleCategoryMapper.queryArticleCategoryById(new ArrayList<String>() {{
            add(articleCategoryId);
        }});
        if (targetArticleCategoryList.size() < 1) {
            return null;
        } else {
            ArticleCategory result = targetArticleCategoryList.get(0);
            return result;
        }
    }

    public ArticleCategoryDTO articleCategoryToDTO(ArticleCategory articleCategory) {
        return new ArticleCategoryDTO(articleCategory);
    }

    public ArrayList<ArticleCategoryDTO> articleCategoryToDTO(List<ArticleCategory> articleCategoryList) {
        ArrayList<ArticleCategoryDTO> result = new ArrayList();
        for (ArticleCategory temp : articleCategoryList) {
            result.add(new ArticleCategoryDTO(temp));
        }
        return result;
    }
}