package org.xavier.blog.article.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.xavier.blog.article.dao.ArticleCategoryMapper;
import org.xavier.blog.article.domain.bo.UserValidateBO;
import org.xavier.blog.article.domain.dto.ArticleCategoryDTO;
import org.xavier.blog.article.domain.enums.ArticleAccessPermitEnum;
import org.xavier.blog.article.domain.enums.UserSexEnum;
import org.xavier.blog.article.domain.enums.UserTypeEnum;
import org.xavier.blog.article.domain.po.article.ArticleCategory;
import org.xavier.blog.article.domain.po.board.Board;
import org.xavier.blog.article.service.remote.UserServiceImpl;
import org.xavier.blog.common.ErrorCode;
import org.xavier.common.enums.ColumnType;
import org.xavier.common.exception.Universal_403_X_Exception;
import org.xavier.common.exception.Universal_404_X_Exception;
import org.xavier.common.logging.HyggeLoggerMsgBuilder;
import org.xavier.common.utils.UtilsCreator;
import org.xavier.common.utils.bo.ColumnInfo;
import org.xavier.web.extend.DefaultService;

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
public class ArticleCategoryServiceImpl extends DefaultService {
    @Autowired
    ArticleCategoryMapper articleCategoryMapper;
    @Autowired
    UserServiceImpl userService;
    @Autowired
    BoardServiceImpl boardService;

    private static List<ColumnInfo> checkInfo;

    static {
        checkInfo = new ArrayList<ColumnInfo>() {{
            add(new ColumnInfo(ColumnType.STRING, "boardId", "boardId", false, 1, 32));
            add(new ColumnInfo(ColumnType.STRING, "articleCategoryName", "articleCategoryName", false, 1, 30));
            add(new ColumnInfo(ColumnType.STRING, "description", "description", true, 0, 2000));
            add(new ColumnInfo(ColumnType.STRING, "uId", "uId", false, 1, 10));
            add(new ColumnInfo(ColumnType.BYTE, "accessPermit", "accessPermit", false, 0, 6));
        }};
    }

    /**
     * 创建文章类别
     *
     * @param articleCategory 文章类别实体
     * @param currentTs       瞬时时间戳
     */
    public Boolean saveArticleCategory(ArticleCategory articleCategory, Long currentTs) throws Universal_403_X_Exception, Universal_404_X_Exception {
        articleCategory.setLegal_Flag(true);
        articleCategory.setTs(currentTs);
        articleCategory.setLastUpdateTs(currentTs);
        articleCategory.setArticleCategoryId(UtilsCreator.getInstance_DefaultRandomHelper().getUUID());
        switch (articleCategory.getAccessPermit()) {
            case PERSONAL:
                articleCategory.setExtendProperties(articleCategory.getuId());
                break;
            case SECRET_KEY:
                articleCategory.setExtendProperties(UtilsCreator.getInstance_DefaultRandomHelper().getUUID());
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
        Integer saveArticleCategory_EffectedLine = articleCategoryMapper.saveArticleCategory(articleCategory);
        Boolean saveArticleCategory_Flag = saveArticleCategory_EffectedLine == 1;
        if (!saveArticleCategory_Flag) {
            logger.warn(HyggeLoggerMsgBuilder.assertFail("saveArticleCategory_EffectedLine", "1", saveArticleCategory_EffectedLine, board));
        }
        return saveArticleCategory_Flag;
    }

    /**
     * 根据 用户唯一标识、文章类别唯一标识 逻辑删除类别
     *
     * @param articleCategoryIdList 文章类别唯一标识 List
     * @param upTs                  瞬时时间戳
     */
    public Boolean removeArticleCategoryMultipleByIds_Logically(String operatorUId, ArrayList<String> articleCategoryIdList, Long upTs) {
        ArrayList<String> articleCategoryIdListForQuery = listHelper.filterStringListNotEmpty(articleCategoryIdList, "articleCategoryId", 32, 32);
        articleCategoryIdList = articleCategoryMapper.queryArticleCategoryIdOfUser(operatorUId, articleCategoryIdListForQuery);
        articleCategoryIdListForQuery = listHelper.filterStringListNotEmpty(articleCategoryIdList, "articleCategoryId", 32, 32);
        Integer removeArticleCategoryMultiple_EffectedLine = articleCategoryMapper.removeArticleCategoryMultipleByArticleCategoryId_Logically(operatorUId, articleCategoryIdListForQuery, upTs);
        Boolean removeArticleCategoryMultiple_Flag = removeArticleCategoryMultiple_EffectedLine == articleCategoryIdListForQuery.size();
        if (!removeArticleCategoryMultiple_Flag) {
            ArrayList<String> finalArticleCategoryIdListForQuery = articleCategoryIdListForQuery;
            logger.warn(HyggeLoggerMsgBuilder.assertFail("removeArticleCategoryMultiple_EffectedLine", "1", removeArticleCategoryMultiple_EffectedLine, new LinkedHashMap() {{
                put("operatorUId", operatorUId);
                put("articleCategoryIdList", finalArticleCategoryIdListForQuery);
                put("upTs", upTs);
            }}));
        }
        return removeArticleCategoryMultiple_Flag;
    }


    /**
     * 根据 类别唯一标识 更新类别
     *
     * @param articleCategoryId 类别唯一表示
     * @param rowData           更新数据
     */
    public Boolean updateArticleCategory(String operatorUId, String articleCategoryId, Map rowData, Long upTs) throws Universal_403_X_Exception, Universal_404_X_Exception {
        HashMap data = sqlHelper.createFinalUpdateDataWithTimeStamp(rowData, checkInfo, LASTUPDATETS);
        mapHelper.mapNotEmpty(data, "Effective Update-Info was null.");
        ArticleCategory articleCategory = queryArticleCategoryById_WithExistValidate(articleCategoryId);
        userService.checkRight(operatorUId, articleCategory.getuId());
        if (rowData.containsKey("boardId")) {
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
    public ArticleCategory queryArticleCategoryById_WithExistValidate(String articleCategoryId) throws Universal_404_X_Exception {
        ArticleCategory targetArticleCategory = queryArticleCategoryByArticleCategoryId(articleCategoryId);
        if (targetArticleCategory == null) {
            throw new Universal_404_X_Exception(ErrorCode.ARTICLECATEGORY_NOTFOUND.getErrorCod(), "ArticleCategory(" + articleCategoryId + ") was not found.");
        }
        return targetArticleCategory;
    }

    /**
     * 根据 articleCategoryId 查询对象
     */
    public ArticleCategory queryArticleCategoryByArticleCategoryId(String articleCategoryId) {
        ArrayList<ArticleCategory> targetArticleCategoryList = articleCategoryMapper.queryArticleCategoryById(listHelper.createSingleList(articleCategoryId));
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