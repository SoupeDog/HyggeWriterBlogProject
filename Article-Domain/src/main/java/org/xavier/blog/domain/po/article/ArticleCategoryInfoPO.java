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
    private String articleCategoryNo;
    /**
     * 文章类别名称
     */
    private String articleCategoryName;


    public ArticleCategoryInfoPO() {
    }

    /**
     * @param data 形如 {文章类别唯一标识}-{文章类别名称}
     */
    public ArticleCategoryInfoPO(String data) {
        String[] array = data.split("-");
        this.articleCategoryNo = array[0];
        this.articleCategoryName = array[1];
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
}