package org.xavier.blog.article.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.xavier.blog.article.domain.po.article.Article;

import java.util.ArrayList;
import java.util.Map;

/**
 * 描述信息：<br/>
 * 文章操作
 *
 * @author Xavier
 * @version 1.0
 * @date 2018/3/11
 * @since Jdk 1.8
 */
@Mapper
public interface ArticleMapper {

    /**
     * 创建文章
     *
     * @param article 文章实体
     * @return 受影响行
     */
    Integer saveArticle(@Param("article") Article article);

    /**
     * 逻辑删除文章
     *
     * @param articleIdList 文章唯一标识 List
     * @return 受影响行
     */
    Integer removeArticleMultipleByIds_Logically(@Param("articleIdList") ArrayList<String> articleIdList, @Param("uId") String uId, @Param("lastUpdateTs") Long lastUpdateTs);

    /**
     * @param articleId 文章唯一标识
     * @param pageViews 浏览量
     * @return 受影响行
     */
    Integer updateArticlePageViews(@Param("articleId") Integer articleId, @Param("pageViews") Integer pageViews, @Param("lastUpdateTs_CAS") Long lastUpdateTs_CAS);


    /**
     * 自增文章浏览量 1
     *
     * @param articleId 文章唯一标识
     * @return 受影响行
     */
    Integer autoIncreaseArticlePageViews(@Param("articleId") String articleId);

    /**
     * @param articleId 文章唯一标识
     * @param data      修改数据
     * @return 受影响行
     */
    Integer updateArticle(@Param("articleId") String articleId, @Param("data") Map data, @Param("lastUpdateTs_CAS") Long lastUpdateTs_CAS);

    /**
     * 根据 id 批量查询文章信息
     *
     * @param articleIdList 文章唯一标识 List
     * @return 查询结果集
     */
    ArrayList<Article> queryArticleListByIds(@Param("articleIdList") ArrayList<String> articleIdList);

    /**
     * 在限制范围内查询目标用户名下文章的 ArticleId 列表
     *
     * @param uId                    目标用户唯一表示
     * @param rangeArticleIdListList ArticleId 限制范围
     */
    ArrayList<String> queryArticleIdLisOfUser(@Param("uId") String uId, @Param("rangeArticleIdListList") ArrayList<String> rangeArticleIdListList);

}