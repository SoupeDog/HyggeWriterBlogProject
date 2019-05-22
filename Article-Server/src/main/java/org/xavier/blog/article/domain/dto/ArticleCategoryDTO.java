package org.xavier.blog.article.domain.dto;


import org.xavier.blog.article.domain.po.article.ArticleCategory;

/**
 * 描述信息：<br/>
 *
 * @author Xavier
 * @version 1.0
 * @date 2018/6/14
 * @since Jdk 1.8
 */
public class ArticleCategoryDTO {
    /**
     * 唯一标示
     */
    private String articleCategoryId;
    /**
     * 作者唯一标识
     */
    private String uId;
    /**
     * 板块唯一标识
     */
    private String boardId;
    /**
     * 类别名称
     */
    private String articleCategoryName;
    /**
     * 文章类别简介
     */
    private String description;
    /**
     * 最后修改时间 utc 毫秒级时间戳
     */
    private Long lastUpdateTs;
    /**
     * 创建时间 utc 毫秒级时间戳
     */
    private Long ts;

    public ArticleCategoryDTO() {
    }

    public ArticleCategoryDTO(ArticleCategory articleCategory) {
        this.articleCategoryId = articleCategory.getArticleCategoryId();
        this.uId = articleCategory.getuId();
        this.boardId = articleCategory.getBoardId();
        this.articleCategoryName = articleCategory.getArticleCategoryName();
        this.description = articleCategory.getDescription();
        this.lastUpdateTs = articleCategory.getLastUpdateTs();
        this.ts = articleCategory.getTs();
    }

    public String getArticleCategoryId() {
        return articleCategoryId;
    }

    public void setArticleCategoryId(String articleCategoryId) {
        this.articleCategoryId = articleCategoryId;
    }

    public String getuId() {
        return uId;
    }

    public void setuId(String uId) {
        this.uId = uId;
    }

    public String getBoardId() {
        return boardId;
    }

    public void setBoardId(String boardId) {
        this.boardId = boardId;
    }

    public String getArticleCategoryName() {
        return articleCategoryName;
    }

    public void setArticleCategoryName(String articleCategoryName) {
        this.articleCategoryName = articleCategoryName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getLastUpdateTs() {
        return lastUpdateTs;
    }

    public void setLastUpdateTs(Long lastUpdateTs) {
        this.lastUpdateTs = lastUpdateTs;
    }

    public Long getTs() {
        return ts;
    }

    public void setTs(Long ts) {
        this.ts = ts;
    }
}
