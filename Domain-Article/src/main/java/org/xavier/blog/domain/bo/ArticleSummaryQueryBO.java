package org.xavier.blog.domain.bo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.xavier.blog.common.enums.DefaultStateEnum;
import org.xavier.blog.domain.po.Article;
import org.xavier.blog.domain.po.ArticleCategory;
import org.xavier.blog.domain.po.Board;
import org.xavier.blog.domain.po.article.ArticleCategoryInfoPO;
import org.xavier.common.exception.PropertiesRuntimeException;
import org.xavier.common.util.UtilsCreator;

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
     * 文章显示标示
     */
    private String articleNo;
    /**
     * 板块显示标示
     */
    private String boardNo;
    /**
     * 板块名称
     */
    private String boardName;
    /**
     * 文章类别显示编号
     */
    private String articleCategoryNo;
    /**
     * 板块名称
     */
    private String articleCategoryName;
    /**
     * 父类文章类别信息
     */
    private ArrayList<ArticleCategoryInfoPO> parentArticleCategoryList;
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
     * 文章额外参数
     */
    private ArticleJsonProperties properties;
    /**
     * 是否激活标识 1 禁用 2 启用
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

    public ArticleSummaryQueryBO() {
    }

    public ArticleSummaryQueryBO(Article article, Board board, ArticleCategory articleCategory, ArrayList<ArticleCategoryInfoPO> parentArticleCategoryList) {
        this.articleNo = article.getArticleNo();
        this.boardNo = board.getBoardNo();
        this.boardName = board.getBoardName();
        this.articleCategoryNo = articleCategory.getArticleCategoryNo();
        this.articleCategoryName = articleCategory.getArticleCategoryName();
        this.parentArticleCategoryList = parentArticleCategoryList;
        this.title = article.getTitle();
        this.uid = article.getUid();
        this.summary = article.getSummary();
        this.wordCount = article.getWordCount();
        this.pageViews = article.getPageViews();
        try {
            ObjectMapper objectMapper = (ObjectMapper) UtilsCreator.getDefaultJsonHelperInstance(false).getDependence();
            this.properties = objectMapper.readValue(article.getProperties(), ArticleJsonProperties.class);
        } catch (JsonProcessingException e) {
            throw new PropertiesRuntimeException("Fail to read:" + article.getProperties());
        }
        this.state = article.getState();
        this.createTs = article.getCreateTs();
        this.lastUpdateTs = article.getLastUpdateTs();
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

    public String getArticleCategoryName() {
        return articleCategoryName;
    }

    public void setArticleCategoryName(String articleCategoryName) {
        this.articleCategoryName = articleCategoryName;
    }

    public ArrayList<ArticleCategoryInfoPO> getParentArticleCategoryList() {
        return parentArticleCategoryList;
    }

    public void setParentArticleCategoryList(ArrayList<ArticleCategoryInfoPO> parentArticleCategoryList) {
        this.parentArticleCategoryList = parentArticleCategoryList;
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

    public ArticleJsonProperties getProperties() {
        return properties;
    }

    public void setProperties(ArticleJsonProperties properties) {
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