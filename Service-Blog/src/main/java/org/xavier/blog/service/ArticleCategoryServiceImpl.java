package org.xavier.blog.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.xavier.blog.common.ErrorCode;
import org.xavier.blog.common.enums.UserTypeEnum;
import org.xavier.blog.dao.ArticleCategoryMapper;
import org.xavier.blog.domain.po.ArticleCategory;
import org.xavier.blog.domain.po.User;
import org.xavier.blog.domain.po.article.ArticleCategoryInfoPO;
import org.xavier.common.enums.ColumnType;
import org.xavier.common.exception.Universal400Exception;
import org.xavier.common.exception.Universal403Exception;
import org.xavier.common.exception.Universal404Exception;
import org.xavier.common.logging.HyggeLoggerMsgBuilder;
import org.xavier.common.util.UtilsCreator;
import org.xavier.common.util.bo.ColumnInfo;
import org.xavier.webtoolkit.base.DefaultUtils;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 描述信息：<br/>
 *
 * @author Xavier
 * @version 1.0
 * @date 19-12-29
 * @since Jdk 1.8
 */
@Service
public class ArticleCategoryServiceImpl extends DefaultUtils {
    @Autowired
    ArticleCategoryMapper articleCategoryMapper;
    @Autowired
    UserServiceImpl userService;

    public static ConcurrentHashMap<String, ArrayList<ArticleCategoryInfoPO>> articleCategoryTreeInfo = new ConcurrentHashMap<>();

    private static final List<ColumnInfo> CHECK_INFO = new ArrayList<ColumnInfo>() {{
        add(new ColumnInfo("articleCategoryName", "articleCategoryName", ColumnType.STRING, false, 1, 32));
        add(new ColumnInfo("uid", "uid", ColumnType.STRING, false, 1, 32));
        add(new ColumnInfo("accessPermit", "accessPermit", ColumnType.BYTE, false, 0, Byte.MAX_VALUE));
    }};

    public Boolean saveArticleCategory(ArticleCategory articleCategory, Long currentTs) {
        articleCategory.setCreateTs(currentTs);
        articleCategory.setLastUpdateTs(currentTs);
        articleCategory.setArticleCategoryNo(UtilsCreator.getDefaultRandomHelperInstance().getUniversallyUniqueIdentifier());
        Integer saveArticleCategoryAffectedRow = articleCategoryMapper.saveArticleCategory(articleCategory);
        Boolean articleCategoryFlag = saveArticleCategoryAffectedRow == 1;
        if (!articleCategoryFlag) {
            logger.warn(HyggeLoggerMsgBuilder.assertFail("saveArticleCategoryAffectedRow", "1", saveArticleCategoryAffectedRow, articleCategory));
        }
        return articleCategoryFlag;
    }

    public Boolean updateArticleCategory(String loginUid, String articleCategoryNo, Map rawData, Long upTs) throws Universal404Exception, Universal400Exception, Universal403Exception {
        ArticleCategory targetArticleCategory = queryArticleCategoryNoByArticleCategoryNoNotNull(articleCategoryNo);
        User loginUser = userService.queryUserNotNull(loginUid);
        userService.checkRight(loginUser, UserTypeEnum.ROOT, targetArticleCategory.getUid());
        HashMap data = sqlHelper.createFinalUpdateDataWithDefaultTsColumn(upTs, rawData, CHECK_INFO);
        if (data.size() < 2) {
            throw new Universal400Exception(ErrorCode.UPDATE_DATA_EMPTY.getErrorCod(), "Effective-Update-Properties can't be empty.");
        }
        Integer updateArticleCategoryRow = articleCategoryMapper.updateArticleCategoryByArticleCategoryNo(articleCategoryNo, data, upTs);
        Boolean updateArticleCategoryFlag = updateArticleCategoryRow == 1;
        if (!updateArticleCategoryFlag) {
            logger.warn(HyggeLoggerMsgBuilder.assertFail("updateArticleCategoryRow", "1", updateArticleCategoryRow, new LinkedHashMap() {{
                put("loginUid", loginUid);
                put("articleCategoryNo", articleCategoryNo);
                put("rawData", rawData);
            }}));
        }
        return updateArticleCategoryFlag;
    }

    public ArrayList<ArticleCategory> queryAllArticleCategory(ArrayList<Byte> accessPermitRangeList) {
        ArrayList<ArticleCategory> result = articleCategoryMapper.queryAllArticleCategory(accessPermitRangeList);
        return result;
    }

    public ArticleCategory queryArticleCategoryNoByArticleCategoryNo(String articleCategoryNo) {
        propertiesHelper.stringNotNull(articleCategoryNo, 0, 32, "[articleCategoryNo] can't be null,and it should be a string[0,32].");
        ArticleCategory result = articleCategoryMapper.queryArticleCategoryNoByArticleCategoryNo(articleCategoryNo);
        return result;
    }

    public ArticleCategory queryArticleCategoryNoByArticleCategoryNoNotNull(String articleCategoryNo) throws Universal404Exception {
        ArticleCategory result = queryArticleCategoryNoByArticleCategoryNo(articleCategoryNo);
        if (result == null) {
            throw new Universal404Exception(ErrorCode.ARTICLECATEGORY_NOTFOUND.getErrorCod(), "ArticleCategory(" + articleCategoryNo + ") was not found.");
        }
        return result;
    }


}