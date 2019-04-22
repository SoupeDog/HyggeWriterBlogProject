package org.xavier.blog.article.domain.po.statement;

import org.xavier.common.utils.PropertiesHelper;
import org.xavier.common.utils.UtilsCreator;

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
     * 版权声明合法性标识 null:待审核 true:合法 false:非法
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
        propertiesHelper.stringNotNull(uId, 9, 10, "[uId] can't be null,and its length should be between 9~10.");
        propertiesHelper.stringNotNull(content, "[content] can't be null,and its length should within 1000.");
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
