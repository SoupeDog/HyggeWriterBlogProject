package org.xavier.blog.article.domain.po.statement;


import org.xavier.common.util.PropertiesHelper;
import org.xavier.common.util.UtilsCreator;

/**
 * 描述信息：<br/>
 * 版权声明
 *
 * @author Xavier
 * @version 1.0
 * @date 2017/12/7
 * @since Jdk 1.8
 */
public class Statement {

    /**
     * 版权声明唯一标识
     */
    private String statementId;

    /**
     * 作者唯一标识
     */
    private String uId;

    /**
     * 版权申明内容
     */
    private String content;
    /**
     * 额外配置参数
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

    /**
     * 参数校验
     */
    public void validate() {
        PropertiesHelper propertiesHelper = UtilsCreator.getDefaultPropertiesHelperInstance();
        propertiesHelper.stringNotNull(content, 1, 1000, "[content] can't be null,and its length should within 1000.");
    }

    public String getStatementId() {
        return statementId;
    }

    public void setStatementId(String statementId) {
        this.statementId = statementId;
    }

    public String getuId() {
        return uId;
    }

    public void setuId(String uId) {
        this.uId = uId;
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
