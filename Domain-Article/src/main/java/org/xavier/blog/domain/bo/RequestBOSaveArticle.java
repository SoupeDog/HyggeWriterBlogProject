package org.xavier.blog.domain.bo;


/**
 * 描述信息：<br/>
 *
 * @author Xavier
 * @version 1.0
 * @date 19-12-29
 * @since Jdk 1.8
 */
public class RequestBOSaveArticle {
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
     * 摘要内容
     */
    private String summary;
    /**
     * 文章内容
     */
    private String content;
    /**
     * 文章额外参数(Json)
     */
    private String properties;


    public RequestBOSaveArticle() {
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

    public String getProperties() {
        return properties;
    }

    public void setProperties(String properties) {
        this.properties = properties;
    }
}