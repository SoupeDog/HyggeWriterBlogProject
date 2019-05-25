package org.xavier.blog.article.domain.dto;

import org.xavier.blog.article.domain.po.article.Article;

/**
 * 描述信息：<br/>
 * 文章信息数据传输层对象
 *
 * @author Xavier
 * @version 1.0
 * @date 2018/6/18
 * @since Jdk 1.8
 */
public class ArticleDTO {
    /**
     * 作者唯一标识
     */
    private String uId;
    /**
     * 唯一标示
     */
    private String articleId;
    /**
     * 文章类别唯一标识
     */
    private String articleCategoryId;
    /**
     * 版权声明唯一表示
     */
    private String statementId;
    /**
     * 文章标题
     */
    private String title;

    /**
     * 摘要内容
     */
    private String summary;
    /**
     * 文章内容
     */
    private String content;
    /**
     * 字数统计
     */
    private Integer wordCount;
    /**
     * 浏览量
     */
    private Integer pageViews;

    /**
     * 文章额外参数(Json)
     */
    private String properties;
    /**
     * 最后修改时间 utc 毫秒级时间戳
     */
    private Long lastUpdateTs;
    /**
     * 创建时间 utc 毫秒级时间戳
     */
    private Long ts;

    public ArticleDTO(Article article) {
        this.uId = article.getuId();
        this.articleId = article.getArticleId();
        this.articleCategoryId = article.getArticleCategoryId();
        this.statementId = article.getStatementId();
        this.title = article.getTitle();
        this.summary = article.getSummary();
        this.content = article.getContent();
        this.wordCount = article.getWordCount();
        this.pageViews = article.getPageViews();
        this.properties = article.getProperties();
        this.lastUpdateTs = article.getLastUpdateTs();
        this.ts = article.getTs();
    }

    public String getuId() {
        return uId;
    }

    public void setuId(String uId) {
        this.uId = uId;
    }

    public String getArticleId() {
        return articleId;
    }

    public void setArticleId(String articleId) {
        this.articleId = articleId;
    }

    public String getArticleCategoryId() {
        return articleCategoryId;
    }

    public void setArticleCategoryId(String articleCategoryId) {
        this.articleCategoryId = articleCategoryId;
    }

    public String getStatementId() {
        return statementId;
    }

    public void setStatementId(String statementId) {
        this.statementId = statementId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getWordCount() {
        return wordCount;
    }

    public void setWordCount(Integer wordCount) {
        this.wordCount = wordCount;
    }

    public Integer getPageViews() {
        return pageViews;
    }

    public void setPageViews(Integer pageViews) {
        this.pageViews = pageViews;
    }

    public String getProperties() {
        return properties;
    }

    public void setProperties(String properties) {
        this.properties = properties;
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