package org.xavier.blog.domain.bo;

import org.xavier.blog.domain.po.Article;
import org.xavier.blog.domain.po.article.ArticleCategoryInfoPO;

import java.util.ArrayList;

/**
 * 描述信息：<br/>
 * 文章简介查询 BO
 *
 * @author Xavier
 * @version 1.0
 * @date 2019/5/26
 * @since Jdk 1.8
 */
public class ArticleSummaryQueryBO {
    /**
     * 唯一标示
     */
    private String articleNo;
    /**
     * 板块名称
     */
    private String boardName;
    /**
     * 文章类别唯一标识
     */
    private String articleCategoryNo;
    /**
     * 文章标题
     */
    private String title;
    /**
     * 作者唯一标识
     */
    private String uId;
    /**
     * 文章类别节点路径
     */
    private ArrayList<ArticleCategoryInfoPO> articleCategoryPath;
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
    private Long createTs;

    public ArticleSummaryQueryBO() {
    }

    public ArticleSummaryQueryBO(Article article) {
        this.articleNo = article.getArticleNo();
//        this.boardName = boardName;
//        this.articleCategoryNo = articleCategoryNo;
        this.title = article.getTitle();
        this.uId = article.getUid();
        this.summary = article.getSummary();
        this.wordCount = article.getWordCount();
        this.pageViews = article.getPageViews();
        this.properties = article.getProperties();
        this.lastUpdateTs = article.getLastUpdateTs();
        this.createTs = article.getCreateTs();
    }

    public String getArticleNo() {
        return articleNo;
    }

    public void setArticleNo(String articleNo) {
        this.articleNo = articleNo;
    }

    public String getBoardName() {
        return boardName;
    }

    public void setBoardName(String boardName) {
        this.boardName = boardName;
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

    public String getuId() {
        return uId;
    }

    public void setuId(String uId) {
        this.uId = uId;
    }

    public ArrayList<ArticleCategoryInfoPO> getArticleCategoryPath() {
        return articleCategoryPath;
    }

    public void setArticleCategoryPath(ArrayList<ArticleCategoryInfoPO> articleCategoryPath) {
        this.articleCategoryPath = articleCategoryPath;
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

    public Long getCreateTs() {
        return createTs;
    }

    public void setCreateTs(Long createTs) {
        this.createTs = createTs;
    }
}