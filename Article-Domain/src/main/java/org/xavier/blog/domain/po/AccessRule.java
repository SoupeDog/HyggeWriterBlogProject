package org.xavier.blog.domain.po;

import org.xavier.blog.common.enums.ArticleAccessPermitEnum;

/**
 * 描述信息：<br/>
 *
 * @author Xavier
 * @version 1.0
 * @date 19-12-30
 * @since Jdk 1.8
 */
public class AccessRule {
    /**
     * 规则唯一标识
     */
    private Integer accessRuleId;
    /**
     * 唯一标示
     */
    private Integer articleCategoryId;
    /**
     * 文章类别显示标示
     */
    private String articleCategoryNo;

    /**
     * 是否为必要条件
     */
    private Boolean requirement;

    /**
     * 访问许可类型
     */
    private ArticleAccessPermitEnum accessPermit;
    /**
     * 扩展用参数 根据许可类型不同可能为 秘钥、Cron 表达式、群组唯一标识……
     */
    private String extendProperties;

    /**
     * 创建时间 utc 毫秒级时间戳
     */
    private Long createTs;

    /**
     * 最后修改时间 utc 毫秒级时间戳
     */
    private Long lastUpdateTs;

    public Integer getAccessRuleId() {
        return accessRuleId;
    }

    public void setAccessRuleId(Integer accessRuleId) {
        this.accessRuleId = accessRuleId;
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

    public Boolean getRequirement() {
        return requirement;
    }

    public void setRequirement(Boolean requirement) {
        this.requirement = requirement;
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
