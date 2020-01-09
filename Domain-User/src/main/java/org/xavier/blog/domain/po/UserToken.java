package org.xavier.blog.domain.po;


import org.xavier.blog.common.enums.UserTokenScopeEnum;
import org.xavier.common.util.UtilsCreator;

/**
 * 描述信息：<br/>
 * 用户 token
 *
 * @author Xavier
 * @version 1.0
 * @date 2018/6/26
 * @since Jdk 1.8
 */
public class UserToken {
    /**
     * 用户唯一标识
     */
    private String uid;
    /**
     * 当前 token
     */
    private String token;

    /**
     * 当前 token 过期时间
     */
    private Long deadLine;

    /**
     * token 作用域
     */
    private UserTokenScopeEnum scope;
    /**
     * 上一个 token
     */
    private String lastToken;

    /**
     * 上一个 token 过期时间
     */
    private Long lastDeadLine;

    /**
     * 刷新秘钥
     */
    private String refreshKey;

    /**
     * 创建 UTC 毫秒级时间戳
     */
    private Long createTs;
    /**
     * 最后修改 UTC 毫秒级时间戳
     */
    private Long lastUpdateTs;

    public void firstInit(String uId, UserTokenScopeEnum scope, Long currentTs) {
        this.uid = uId;
        this.token = UtilsCreator.getDefaultRandomHelperInstance().getUniversallyUniqueIdentifier();
        this.deadLine = currentTs + 7200000L;
        this.lastToken = "";
        this.lastDeadLine = 0L;
        this.scope = scope;
        this.createTs = currentTs;
        this.lastUpdateTs = currentTs;
        this.refreshKey = UtilsCreator.getDefaultRandomHelperInstance().getUniversallyUniqueIdentifier();
    }

    public void refresh(Long currentTs) {
        this.lastToken = this.token;
        this.lastDeadLine = currentTs + 120000L;
        this.token = UtilsCreator.getDefaultRandomHelperInstance().getUniversallyUniqueIdentifier();
        this.deadLine = currentTs + 7200000L;
        this.refreshKey = UtilsCreator.getDefaultRandomHelperInstance().getUniversallyUniqueIdentifier();
        this.lastUpdateTs = currentTs;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Long getDeadLine() {
        return deadLine;
    }

    public void setDeadLine(Long deadLine) {
        this.deadLine = deadLine;
    }

    public UserTokenScopeEnum getScope() {
        return scope;
    }

    public void setScope(UserTokenScopeEnum scope) {
        this.scope = scope;
    }

    public String getLastToken() {
        return lastToken;
    }

    public void setLastToken(String lastToken) {
        this.lastToken = lastToken;
    }

    public Long getLastDeadLine() {
        return lastDeadLine;
    }

    public void setLastDeadLine(Long lastDeadLine) {
        this.lastDeadLine = lastDeadLine;
    }

    public String getRefreshKey() {
        return refreshKey;
    }

    public void setRefreshKey(String refreshKey) {
        this.refreshKey = refreshKey;
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
