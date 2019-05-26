package org.xavier.blog.article.domain.po.article;

/**
 * 描述信息：<br/>
 * 文章用户查询业务对象
 *
 * @author Xavier
 * @version 1.0
 * @date 2019/5/26
 * @since Jdk 1.8
 */
public class ArticleQuarryPO {
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
     * 版权申明内容
     */
    private String statementContent;
    /**
     * 版权声明额外配置参数
     */
    private String statementProperties;
    /**
     * 文章标题
     */
    private String title;

    /**
     * 板块名称
     */
    private String boardName;
    /**
     * 文章类别节点路径
     */
    private String articleCategoryPathStringVal;
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

    public String getArticleId() {
        return articleId;
    }

    public void setArticleId(String articleId) {
        this.articleId = articleId;
    }

    public String getuId() {
        return uId;
    }

    public void setuId(String uId) {
        this.uId = uId;
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

    public String getStatementContent() {
        return statementContent;
    }

    public void setStatementContent(String statementContent) {
        this.statementContent = statementContent;
    }

    public String getStatementProperties() {
        return statementProperties;
    }

    public void setStatementProperties(String statementProperties) {
        this.statementProperties = statementProperties;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBoardName() {
        return boardName;
    }

    public void setBoardName(String boardName) {
        this.boardName = boardName;
    }

    public String getArticleCategoryPathStringVal() {
        return articleCategoryPathStringVal;
    }

    public void setArticleCategoryPathStringVal(String articleCategoryPathStringVal) {
        this.articleCategoryPathStringVal = articleCategoryPathStringVal;
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