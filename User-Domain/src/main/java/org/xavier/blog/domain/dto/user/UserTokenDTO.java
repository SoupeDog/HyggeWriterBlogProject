package org.xavier.blog.domain.dto.user;


import org.xavier.blog.common.enums.UserTokenScopeEnum;
import org.xavier.blog.domain.po.UserToken;

/**
 * 描述信息：<br/>
 * token 数据传输层对象
 *
 * @author Xavier
 * @version 1.0
 * @date 2018/7/4
 * @since Jdk 1.8
 */
public class UserTokenDTO {
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
     * 刷新秘钥
     */
    private String refreshKey;
    /**
     * token 作用域
     */
    private UserTokenScopeEnum scope;


    public UserTokenDTO() {
    }

    public UserTokenDTO(UserToken userToken) {
        this.uid = userToken.getUid();
        this.token = userToken.getToken();
        this.deadLine = userToken.getDeadLine();
        this.refreshKey = userToken.getRefreshKey();
        this.scope = userToken.getScope();
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

    public String getRefreshKey() {
        return refreshKey;
    }

    public void setRefreshKey(String refreshKey) {
        this.refreshKey = refreshKey;
    }
}
