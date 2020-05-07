package org.xavier.blog.domain.bo;

import org.xavier.blog.domain.po.ArticleCategory;

/**
 * 描述信息：<br/>
 * 文章类别文章计数器
 *
 * @author Xavier
 * @version 1.0
 * @date 20-5-6
 * @since Jdk 1.8
 */
public class ArticleCategoryArticleCountInfo {
    /**
     * 文章类别唯一标识
     */
    private Integer articleCategoryId;
    /**
     * 文章类别显示编号
     */
    private String articleCategoryNo;
    /**
     * 文章类别名称
     */
    private String articleCategoryName;
    /**
     * 文章类别内文章数量
     */
    private Integer totalCount;

    public ArticleCategoryArticleCountInfo() {
    }

    public ArticleCategoryArticleCountInfo(ArticleCategory articleCategory) {
        this.articleCategoryId = articleCategory.getArticleCategoryId();
        this.articleCategoryNo = articleCategory.getArticleCategoryNo();
        this.articleCategoryName = articleCategory.getArticleCategoryName();
    }

    public Integer getArticleCategoryId() {
        return articleCategoryId;
    }

    public void setArticleCategoryId(Integer articleCategoryId) {
        this.articleCategoryId = articleCategoryId;
    }

    public String getArticleCategoryNo() {
        return articleCategoryNo;
    }

    public void setArticleCategoryNo(String articleCategoryNo) {
        this.articleCategoryNo = articleCategoryNo;
    }

    public String getArticleCategoryName() {
        return articleCategoryName;
    }

    public void setArticleCategoryName(String articleCategoryName) {
        this.articleCategoryName = articleCategoryName;
    }

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }
}