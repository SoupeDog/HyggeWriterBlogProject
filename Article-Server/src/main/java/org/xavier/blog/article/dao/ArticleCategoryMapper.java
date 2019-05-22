package org.xavier.blog.article.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.xavier.blog.article.domain.po.article.ArticleCategory;

import java.util.ArrayList;
import java.util.Map;

/**
 * 描述信息：<br/>
 * 文章类别
 *
 * @author Xavier
 * @version 1.0
 * @date 2018/4/3
 * @since Jdk 1.8
 */
@Mapper
public interface ArticleCategoryMapper {
    /**
     * 创建文章类别
     *
     * @param articleCategory 文章类别实体
     * @return 受影响行
     */
    Integer saveArticleCategory(@Param("articleCategory") ArticleCategory articleCategory);

    /**
     * 逻辑删除文章类别
     *
     * @param uId                   用户唯一标识
     * @param articleCategoryIdList 文章类别唯一标识 List 字符串形式
     * @return 受影响行
     */
    Integer removeArticleCategoryMultipleByIds_Logically(@Param("uId") String uId, @Param("articleCategoryIdList") ArrayList<String> articleCategoryIdList, @Param("lastUpdateTs") Long lastUpdateTs);

    /**
     * 更新文章类别
     *
     * @param articleCategoryId 文章类别唯一标识
     * @param data              修改数据
     * @return 受影响行
     */
    Integer updateArticleCategory(@Param("articleCategoryId") String articleCategoryId, @Param("data") Map data, @Param("lastUpdateTs_CAS") Long lastUpdateTs_CAS);

    /**
     * 根据 用户唯一标识 查询其名下的文章类别
     *
     * @param uId                   用户唯一标识
     * @param accessPermitRangeList 许可类型(可空)
     */
    ArrayList<ArticleCategory> queryArticleCategoryByUId(@Param("uId") String uId, @Param("accessPermitRangeList") ArrayList<Byte> accessPermitRangeList);

    /**
     * 根据 板块唯一标识 查询其名下的文章类别
     *
     * @param boardIdRangeList      boardId 限制范围
     * @param accessPermitRangeList 许可类型(可空)
     */
    ArrayList<ArticleCategory> queryArticleCategoryByBoardId(@Param("boardIdRangeList") ArrayList<String> boardIdRangeList, @Param("accessPermitRangeList") ArrayList<Byte> accessPermitRangeList);

    /**
     * 根据 用户唯一标识 查询其名下的文章类别
     *
     * @param articleIdList articleId 限制范围
     */
    ArrayList<ArticleCategory> queryArticleCategoryByArticleId(@Param("articleIdList") ArrayList<String> articleIdList);

    /**
     * 根据 类别唯一标识 查询类别
     *
     * @param articleCategoryIdList 用户唯一标识
     */
    ArrayList<ArticleCategory> queryArticleCategoryById(@Param("articleCategoryIdList") ArrayList<String> articleCategoryIdList);

    /**
     * 在限制范围内查询目标用户名下板块的 ArticleCategoryId 列表
     *
     * @param uId                        目标用户唯一表示
     * @param rangeArticleCategoryIdList ArticleCategoryId 限制范围
     */
    ArrayList<String> queryArticleCategoryIdOfUser(@Param("uId") String uId, @Param("rangeArticleCategoryIdList") ArrayList<String> rangeArticleCategoryIdList);

}
