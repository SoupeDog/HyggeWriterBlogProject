package org.xavier.blog.article.domain.bo;

import org.xavier.blog.article.domain.po.article.ArticleCategoryInfoPO;
import org.xavier.blog.article.domain.po.article.ArticleSummaryQueryPO;

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
    private Long ts;

    public ArticleSummaryQueryBO(ArticleSummaryQueryPO articleSummaryQueryPO) {
        this.articleId = articleSummaryQueryPO.getArticleId();
        this.uId = articleSummaryQueryPO.getuId();
        this.articleCategoryId = articleSummaryQueryPO.getArticleCategoryId();
        this.title = articleSummaryQueryPO.getTitle();
        this.boardName = articleSummaryQueryPO.getBoardName();
        ArrayList<ArticleCategoryInfoPO> articleCategoryPathTemp = new ArrayList();
        if (articleSummaryQueryPO.getArticleCategoryPathStringVal() != null && !articleSummaryQueryPO.getArticleCategoryPathStringVal().trim().equals("")) {
            String[] array = articleSummaryQueryPO.getArticleCategoryPathStringVal().split("/");
            for (String temp : array) {
                articleCategoryPathTemp.add(new ArticleCategoryInfoPO(temp));
            }
        }
        this.articleCategoryPath = articleCategoryPathTemp;
        this.summary = articleSummaryQueryPO.getSummary();
        this.wordCount = articleSummaryQueryPO.getWordCount();
        this.pageViews = articleSummaryQueryPO.getPageViews();
        this.properties = articleSummaryQueryPO.getProperties();
        this.lastUpdateTs = articleSummaryQueryPO.getLastUpdateTs();
        this.ts = articleSummaryQueryPO.getTs();
    }

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

    public Long getTs() {
        return ts;
    }

    public void setTs(Long ts) {
        this.ts = ts;
    }
}