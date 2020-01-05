package org.xavier.blog.domain.po.article;

/**
 * 描述信息：<br/>
 * 文章类别信息
 *
 * @author Xavier
 * @version 1.0
 * @date 2019/5/26
 * @since Jdk 1.8
 */
public class ArticleCategoryInfoPO {
    /**
     * 文章类别唯一标识
     */
    private String articleCategoryId;
    /**
     * 文章类别名称
     */
    private String articleCategoryName;

    /**
     * @param data 形如 {文章类别唯一标识}-{文章类别名称}
     */
    public ArticleCategoryInfoPO(String data) {
        String[] array = data.split("-");
        this.articleCategoryId = array[0];
        this.articleCategoryName = array[1];
    }

    public String getArticleCategoryId() {
        return articleCategoryId;
    }

    public void setArticleCategoryId(String articleCategoryId) {
        this.articleCategoryId = articleCategoryId;
    }

    public String getArticleCategoryName() {
        return articleCategoryName;
    }

    public void setArticleCategoryName(String articleCategoryName) {
        this.articleCategoryName = articleCategoryName;
    }
}