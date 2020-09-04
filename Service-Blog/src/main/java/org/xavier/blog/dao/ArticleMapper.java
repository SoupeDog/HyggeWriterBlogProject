package org.xavier.blog.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.xavier.blog.domain.po.Article;

import java.util.ArrayList;
import java.util.Map;

/**
 * 描述信息：<br/>
 * 文章操作
 *
 * @author Xavier
 * @version 1.0
 * @date 2019/4/5
 * @since Jdk 1.8
 */
@Mapper
public interface ArticleMapper {
    /**
     * 持久化文章
     *
     * @param article 目标文章
     * @return 受影响行
     */
    Integer saveArticle(@Param("Article") Article article);


    /**
     * 根据 articleNo List 批量逻辑删除文章
     *
     * @param articleNoList articleNo List
     * @return 受影响行
     */
    Integer removeArticleMultipleByArticleNoLogically(@Param("articleNoList") ArrayList<String> articleNoList, @Param("lastUpdateTs") Long lastUpdateTs);

    /**
     * 根据 articleNo 更新文章信息
     *
     * @param articleNo    文章唯一标识
     * @param data         修改数据
     * @param lastUpdateTs CAS 用字段
     * @return 受影响行
     */
    Integer updateArticleByArticleNo(@Param("articleNo") String articleNo, @Param("data") Map data, @Param("lastUpdateTs") Long lastUpdateTs);

    /**
     * 根据 articleNo 增加目标文章浏览量
     *
     * @param articleNo 文章唯一标识
     * @param delta     浏览量变量(必须为自然数)
     * @return 查询结果
     */
    @Update("update `article` set pageViews=pageViews+#{delta} where articleNo=#{articleNo}")
    Integer increaseArticlePageViewsByArticleNo(@Param("articleNo") String articleNo, @Param("delta") Integer delta);

    /**
     * 根据 articleNo 查询文章信息
     *
     * @param articleNo 文章唯一标识
     * @return 查询结果
     */
    @Select("select articleId,articleNo,boardNo,articleCategoryNo,title,uid,summary,wordCount,pageViews,content,properties,state,createTs,lastUpdateTs from `article` where articleNo=#{articleNo} and state=2 limit 1")
    Article queryArticleByArticleNo(@Param("articleNo") String articleNo);

    /**
     * 查询特定文章类别下文章数量
     *
     * @param articleCategoryNo 文章类别编号
     * @return 文章数量
     */
    @Select("select count(*) from article where articleCategoryNo=#{articleCategoryNo}")
    Integer queryArticleCategoryArticleCount(@Param("articleCategoryNo") String articleCategoryNo);

    /**
     * 根据 articleNo List 批量查询文章信息
     *
     * @param articleNoList articleNo List
     * @return 查询结果
     */
    ArrayList<Article> queryArticleByArticleNoList(@Param("articleNoList") ArrayList<String> articleNoList);

    /**
     * 根据 articleNo List 批量查询文章简要信息
     *
     * @param articleCategoryNoList articleCategoryNo List
     * @return 查询结果
     */
    ArrayList<Article> queryArticleSummaryByArticleCategoryNo(@Param("articleCategoryNoList") ArrayList<String> articleCategoryNoList, @Param("boardNo") String boardNo, @Param("startPoint") Integer startPoint, @Param("size") Integer size, @Param("orderKey") String orderKey, @Param("order") String order);

    /**
     * 根据 articleNo List 批量查询文章简要信息总记录数
     *
     * @param articleCategoryNoList articleCategoryNo List
     * @return 查询结果
     */
    Integer queryArticleSummaryByArticleCategoryNoTotalCount(@Param("articleCategoryNoList") ArrayList<String> articleCategoryNoList, @Param("boardNo") String boardNo);

    /**
     * 全文检索关键字返回匹配到的文章编号
     *
     * @param keyword               关键字
     * @param articleCategoryNoList 允许的文章类别
     * @param startPoint            分页查询起点
     * @param size                  页容量
     * @return 匹配到的文章编号
     */
    ArrayList<String> queryArticleNoForSearch(@Param("keyword") String keyword, @Param("articleCategoryNoList") ArrayList<String> articleCategoryNoList, @Param("startPoint") Integer startPoint, @Param("size") Integer size);

    /**
     * 全文检索关键字返回匹配到的文章总数
     *
     * @param keyword               关键字
     * @param articleCategoryNoList 允许的文章类别
     * @return 匹配到的文章总数
     */
    Integer queryArticleNoCountForSearch(@Param("keyword") String keyword, @Param("articleCategoryNoList") ArrayList<String> articleCategoryNoList);
}