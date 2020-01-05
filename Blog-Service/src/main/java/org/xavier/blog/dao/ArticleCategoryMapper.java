package org.xavier.blog.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.xavier.blog.domain.po.Article;
import org.xavier.blog.domain.po.ArticleCategory;

import java.util.ArrayList;
import java.util.Map;

/**
 * 描述信息：<br/>
 * 文章类别操作
 *
 * @author Xavier
 * @version 1.0
 * @date 2019/4/5
 * @since Jdk 1.8
 */
@Mapper
public interface ArticleCategoryMapper {
    /**
     * 持久化文章类别
     *
     * @param articleCategory 目标文章类别
     * @return 受影响行
     */
    Integer saveArticleCategory(@Param("ArticleCategory") ArticleCategory articleCategory);


    /**
     * 根据 articleCategoryNo List 批量逻辑删除文章类别
     *
     * @param articleCategoryNoList articleCategoryNo List
     * @return 受影响行
     */
    Integer removeArticleMultipleByArticleCategoryNoLogically(@Param("articleCategoryNoList") ArrayList<String> articleCategoryNoList, @Param("lastUpdateTs") Long lastUpdateTs);

    /**
     * 根据 articleCategoryNo 更新文章类别信息
     *
     * @param articleCategoryNo    文章类别唯一标识
     * @param data         修改数据
     * @param lastUpdateTs CAS 用字段
     * @return 受影响行
     */
    Integer updateArticleCategoryByArticleCategoryNo(@Param("articleCategoryNo") String articleCategoryNo, @Param("data") Map data, @Param("lastUpdateTs") Long lastUpdateTs);

    /**
     * 根据 articleCategoryNo 查询文章类别信息
     *
     * @param articleCategoryNo 文章类别唯一标识
     * @return 查询结果
     */
    @Select("select articleCategoryId,articleCategoryNo,articleCategoryName,uid,description,state,createTs,lastUpdateTs from `articleCategory` where articleCategoryNo=#{articleCategoryNo} and state=1 limit 1")
    ArticleCategory queryArticleCategoryNoByArticleCategoryNo(@Param("articleCategoryNo") String articleCategoryNo);
    /**
     * 根据 accessPermitRangeList 查询文章类别信息
     *
     * @param accessPermitRangeList 文章类别访问类型
     * @return 查询结果
     */
    ArrayList<ArticleCategory> queryAllArticleCategory(@Param("accessPermitRangeList") ArrayList<Byte> accessPermitRangeList);
}