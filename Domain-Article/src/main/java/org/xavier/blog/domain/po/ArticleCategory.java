package org.xavier.blog.domain.po;


import org.xavier.blog.common.enums.DefaultStateEnum;
import org.xavier.common.util.PropertiesHelper;
import org.xavier.common.util.UtilsCreator;

/**
 * 描述信息：<br/>
 * 文章类别
 *
 * @author Xavier
 * @version 1.0
 * @date 2017.10.27
 * @since Jdk 1.8
 */
public class ArticleCategory {
    /**
     * 唯一标示
     */
    private Integer articleCategoryId;
    /**
     * 文章类别显示标示
     */
    private String articleCategoryNo;
    /**
     * 文章类别名称
     */
    private String articleCategoryName;
    /**
     * 板块显示编号
     */
    private String boardNo;
    /**
     * 作者唯一标识
     */
    private String uid;
    /**
     * 文章类别简介
     */
    private String description;
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

    /**
     * 参数校验
     */
    public void validate() {
        PropertiesHelper propertiesHelper = UtilsCreator.getDefaultPropertiesHelperInstance();
        propertiesHelper.stringNotNull(articleCategoryName, 1, 30, "[articleCategoryName] can't be null,and its length should within 30.");
    }

    public Integer getArticleCategoryId() {
        return articleCategoryId;
    }

    public void setArticleCategoryId(Integer articleCategoryId) {
        this.articleCategoryId = articleCategoryId;
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

    public String getBoardNo() {
        return boardNo;
    }

    public void setBoardNo(String boardNo) {
        this.boardNo = boardNo;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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