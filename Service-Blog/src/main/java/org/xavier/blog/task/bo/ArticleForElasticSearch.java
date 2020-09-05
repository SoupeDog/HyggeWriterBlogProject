package org.xavier.blog.task.bo;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.xavier.blog.common.enums.DefaultStateEnum;
import org.xavier.blog.domain.po.Article;

/**
 * 描述信息：<br/>
 *
 * @author Xavier
 * @version 1.0
 * @date 2020/9/4
 * @since Jdk 1.8
 */
@Document(indexName = "article", createIndex = false, shards = 1, replicas = 1)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ArticleForElasticSearch {
    /**
     * 唯一标示
     */
    @Id
    private Integer articleId;
    /**
     * 文章显示标示
     */
    @Field(type = FieldType.Keyword)
    private String articleNo;
    /**
     * 板块显示标示
     */
    @Field(type = FieldType.Keyword)
    private String boardNo;
    /**
     * 文章类别显示编号
     */
    @Field(type = FieldType.Keyword)
    private String articleCategoryNo;
    /**
     * 文章标题
     */
    @Field(type = FieldType.Text, analyzer = "ik_max_word")
    private String title;
    /**
     * 作者唯一标识
     */
    @Field(type = FieldType.Keyword)
    private String uid;
    /**
     * 摘要内容
     */
    @Field(type = FieldType.Text, analyzer = "ik_max_word")
    private String summary;
    /**
     * 字数统计
     */
    @Field(type = FieldType.Integer)
    private Integer wordCount;
    /**
     * 浏览量
     */
    @Field(type = FieldType.Integer)
    private Integer pageViews;
    /**
     * 文章内容
     */
    @Field(type = FieldType.Text, analyzer = "ik_max_word")
    private String content;
    /**
     * 文章额外参数(Json)
     */
    @Field(type = FieldType.Text, analyzer = "ik_max_word")
    private String properties;
    /**
     * 是否激活标识 1 禁用 2 启用
     */
    @Field(type = FieldType.Keyword)
    private DefaultStateEnum state;

    /**
     * 创建时间 utc 毫秒级时间戳
     */
    @Field(type = FieldType.Date)
    private Long createTs;

    /**
     * 最后修改时间 utc 毫秒级时间戳
     */
    @Field(type = FieldType.Date)
    private Long lastUpdateTs;

    public ArticleForElasticSearch() {
    }

    public ArticleForElasticSearch(Article article) {
        this.articleId = article.getArticleId();
        this.articleNo = article.getArticleNo();
        this.boardNo = article.getBoardNo();
        this.articleCategoryNo = article.getArticleCategoryNo();
        this.title = article.getTitle();
        this.uid = article.getUid();
        this.summary = article.getSummary();
        this.wordCount = article.getWordCount();
        this.pageViews = article.getPageViews();
        this.content = article.getContent();
        this.properties = article.getProperties();
        this.state = article.getState();
        this.createTs = article.getCreateTs();
        this.lastUpdateTs = article.getLastUpdateTs();
    }

    public Article toArticle() {
        Article result = new Article();
        result.setArticleId(this.articleId);
        result.setArticleNo(this.articleNo);
        result.setBoardNo(this.boardNo);
        result.setArticleCategoryNo(this.articleCategoryNo);
        result.setTitle(this.title);
        result.setUid(this.uid);
        result.setSummary(this.summary);
        result.setWordCount(this.wordCount);
        result.setPageViews(this.pageViews);
        result.setContent(this.content);
        result.setProperties(this.properties);
        result.setState(this.state);
        result.setCreateTs(this.createTs);
        result.setLastUpdateTs(this.lastUpdateTs);
        return result;
    }

    public Integer getArticleId() {
        return articleId;
    }

    public void setArticleId(Integer articleId) {
        this.articleId = articleId;
    }

    public String getArticleNo() {
        return articleNo;
    }

    public void setArticleNo(String articleNo) {
        this.articleNo = articleNo;
    }

    public String getBoardNo() {
        return boardNo;
    }

    public void setBoardNo(String boardNo) {
        this.boardNo = boardNo;
    }

    public String getArticleCategoryNo() {
        return articleCategoryNo;
    }

    public void setArticleCategoryNo(String articleCategoryNo) {
        this.articleCategoryNo = articleCategoryNo;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getProperties() {
        return properties;
    }

    public void setProperties(String properties) {
        this.properties = properties;
    }

    public DefaultStateEnum getState() {
        return state;
    }

    public void setState(DefaultStateEnum state) {
        this.state = state;
    }

    public Long getCreateTs() {
        return createTs;
    }

    public void setCreateTs(Long createTs) {
        this.createTs = createTs;
    }

    public Long getLastUpdateTs() {
        return lastUpdateTs;
    }

    public void setLastUpdateTs(Long lastUpdateTs) {
        this.lastUpdateTs = lastUpdateTs;
    }
}