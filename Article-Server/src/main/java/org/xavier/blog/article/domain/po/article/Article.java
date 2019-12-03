package org.xavier.blog.article.domain.po.article;


import com.fasterxml.jackson.annotation.JsonIgnore;
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
    private String articleId;
    /**
     * 作者唯一标识
     */
    private String uId;
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
     * 文章合法性标识 null:待审核 true:合法 false:非法
     */
    private Boolean legal_Flag;
    /**
     * 最后修改时间 utc 毫秒级时间戳
     */
    private Long lastUpdateTs;
    /**
     * 创建时间 utc 毫秒级时间戳
     */
    private Long ts;


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
        propertiesHelper.stringNotNull(articleCategoryId, "[articleCategoryId] can't be null.");
        propertiesHelper.stringNotNull(uId, 9, 10, "[uId] can't be null,and its length should be between 9~10.");
        propertiesHelper.string(statementId);
        propertiesHelper.stringNotNull(content, 1, 100000, "[content] can't be null,and its length should within 100000.");
        if (statementId == null) {
            statementId = "";
        }
        if (summary == null) {
            summary = "";
        }
        if (properties == null || properties.equals("")) {
            this.properties = "{\"image\":\"https://s1.ax2x.com/2018/10/24/5XWiJq.jpg\"}";
        }
        initWordCount();
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

    public Boolean getLegal_Flag() {
        return legal_Flag;
    }

    public void setLegal_Flag(Boolean legal_Flag) {
        this.legal_Flag = legal_Flag;
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