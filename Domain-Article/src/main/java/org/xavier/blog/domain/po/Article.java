package org.xavier.blog.domain.po;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.xavier.blog.common.enums.DefaultStateEnum;
import org.xavier.blog.domain.bo.ArticleJsonProperties;
import org.xavier.blog.domain.bo.RequestBOSaveArticle;
import org.xavier.common.util.JsonHelper;
import org.xavier.common.util.PropertiesHelper;
import org.xavier.common.util.UtilsCreator;

/**
 * 描述信息：<br/>
 * 文章 基类
 *
 * @author Xavier
 * @version 1.0
 * @date 2017.10.27
 * @since Jdk 1.8
 */

public class Article {
    /**
     * 唯一标示
     */
    private Integer articleId;
    /**
     * 文章显示标示
     */
    private String articleNo;
    /**
     * 板块显示标示
     */
    private String boardNo;

    /**
     * 文章类别显示编号
     */
    private String articleCategoryNo;
    /**
     * 文章标题
     */
    private String title;
    /**
     * 作者唯一标识
     */
    private String uid;
    /**
     * 摘要内容
     */
    private String summary;
    /**
     * 字数统计
     */
    private Integer wordCount;

    /**
     * 浏览量
     */
    private Integer pageViews;
    /**
     * 文章内容
     */
    private String content;
    /**
     * 文章额外参数(Json)
     */
    private String properties;
    /**
     * 是否激活标识 0 禁用 1 启用
     */
    private DefaultStateEnum state;

    /**
     * 创建时间 utc 毫秒级时间戳
     */
    private Long createTs;

    /**
     * 最后修改时间 utc 毫秒级时间戳
     */
    private Long lastUpdateTs;

    public Article() {
    }

    public Article(RequestBOSaveArticle requestBOSaveArticle, String uid) {
        this.boardNo = requestBOSaveArticle.getBoardNo();
        this.articleCategoryNo = requestBOSaveArticle.getArticleCategoryNo();
        this.title = requestBOSaveArticle.getTitle();
        this.summary = requestBOSaveArticle.getSummary();
        this.content = requestBOSaveArticle.getContent();
        this.properties = requestBOSaveArticle.getProperties();
        this.state = DefaultStateEnum.ACTIVE;
        this.uid = uid;
    }

    /**
     * 字数统计方法
     */
    @JsonIgnore
    public void setWordCount(Article targetArticle) {
        if (targetArticle == null || targetArticle.getContent() == null || targetArticle.getContent().trim().equals("")) {
            wordCount = 0;
        } else {
            wordCount = targetArticle.getContent().trim().length();
        }
    }


    /**
     * 字数统计方法
     */
    @JsonIgnore
    public void initWordCount() {
        if (content == null || content.trim().equals("")) {
            wordCount = 0;
        } else {
            wordCount = content.trim().length();
        }
    }

    /**
     * 参数校验
     */
    public void validate() {
        PropertiesHelper propertiesHelper = UtilsCreator.getDefaultPropertiesHelperInstance();
        propertiesHelper.stringNotNull(title, 1, 50, "[title] can't be null,and its length should within 50.");
        propertiesHelper.stringNotNull(boardNo, "[boardNo] can't be null.");
        propertiesHelper.stringNotNull(articleCategoryNo, "[articleCategoryNo] can't be null.");
        propertiesHelper.stringNotNull(uid, 0, 32, "[uid] can't be null,and its length should be between 9~10.");
        propertiesHelper.stringNotNull(content, 1, 100000, "[content] can't be null,and its length should within 100000.");
        propertiesHelper.stringNotNull(summary, 1, 500, "[summary] can't be null,and its length should within 500.");
        if (properties == null) {
            JsonHelper<ObjectMapper> jsonHelper = UtilsCreator.getDefaultJsonHelperInstance(false);
            ArticleJsonProperties articleJsonProperties = new ArticleJsonProperties();
            articleJsonProperties.setDefaultConfigIfAbsent();
            this.properties = jsonHelper.format(articleJsonProperties);
        }
        initWordCount();
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