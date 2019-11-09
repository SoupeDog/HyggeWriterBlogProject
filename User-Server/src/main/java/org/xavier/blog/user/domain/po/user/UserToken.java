package org.xavier.blog.user.domain.po.user;


import org.xavier.blog.user.domain.enums.UserTokenScopeEnum;
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
    private String uId;
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
    private Long ts;
    /**
     * 最后修改 UTC 毫秒级时间戳
     */
    private Long lastUpdateTs;

    public void firstInit(String uId, UserTokenScopeEnum scope, Long currentTs) {
        this.uId = uId;
        this.token = UtilsCreator.getDefaultRandomHelperInstance().getUniversallyUniqueIdentifier();
        this.deadLine = currentTs + 7200000L;
        this.lastToken = "";
        this.lastDeadLine = 0L;
        this.scope = scope;
        this.ts = currentTs;
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

    public String getuId() {
        return uId;
    }

    public void setuId(String uId) {
        this.uId = uId;
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

    public Long getTs() {
        return ts;
    }

    public void setTs(Long ts) {
        this.ts = ts;
    }

    public Long getLastUpdateTs() {
        return lastUpdateTs;
    }

    public void setLastUpdateTs(Long lastUpdateTs) {
        this.lastUpdateTs = lastUpdateTs;
    }
}
