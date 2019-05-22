package org.xavier.blog.article.domain.po.article;

import org.xavier.blog.article.domain.enums.ArticleAccessPermitEnum;
import org.xavier.common.utils.PropertiesHelper;
import org.xavier.common.utils.UtilsCreator;

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
    private String articleCategoryId;
    /**
     * 作者唯一标识
     */
    private String uId;
    /**
     * 板块名称
     */
    private String boardName;
    /**
     * 板块唯一标识
     */
    private String boardId;
    /**
     * 类别名称
     */
    private String articleCategoryName;
    /**
     * 文章类别简介
     */
    private String description;
    /**
     * 访问许可类型
     */
    private ArticleAccessPermitEnum accessPermit;
    /**
     * 扩展用参数 根据许可类型不同可能为 秘钥、Cron 表达式、群组唯一标识……
     */
    private String extendProperties;
    /**
     * 文章类别合法性标识 null:待审核 true:合法 false:非法
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
     * 参数校验
     */
    public void validate() {
        PropertiesHelper propertiesHelper = UtilsCreator.getInstance_DefaultPropertiesHelper();
        propertiesHelper.stringNotNull(articleCategoryName, 1, 30, "[articleCategoryName] can't be null,and its length should within 30.");
        propertiesHelper.objNotNull(accessPermit, ArticleAccessPermitEnum.class, "[accessPermit] can't be null.");
    }

    public String getArticleCategoryId() {
        return articleCategoryId;
    }

    public void setArticleCategoryId(String articleCategoryId) {
        this.articleCategoryId = articleCategoryId;
    }

    public String getuId() {
        return uId;
    }

    public void setuId(String uId) {
        this.uId = uId;
    }

    public String getBoardName() {
        return boardName;
    }

    public void setBoardName(String boardName) {
        this.boardName = boardName;
    }

    public String getBoardId() {
        return boardId;
    }

    public void setBoardId(String boardId) {
        this.boardId = boardId;
    }

    public String getArticleCategoryName() {
        return articleCategoryName;
    }

    public void setArticleCategoryName(String articleCategoryName) {
        this.articleCategoryName = articleCategoryName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ArticleAccessPermitEnum getAccessPermit() {
        return accessPermit;
    }

    public void setAccessPermit(ArticleAccessPermitEnum accessPermit) {
        this.accessPermit = accessPermit;
    }

    public String getExtendProperties() {
        return extendProperties;
    }

    public void setExtendProperties(String extendProperties) {
        this.extendProperties = extendProperties;
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