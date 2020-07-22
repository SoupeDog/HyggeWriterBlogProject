package org.xavier.blog.utils;

import org.xavier.blog.common.enums.UserTokenScopeEnum;
import org.xavier.blog.domain.po.User;

/**
 * 描述信息：<br/>
 * 请求上下文
 *
 * @author Xavier
 * @version 1.0
 * @date 2020/7/22
 * @since Jdk 1.8
 */
public class HyggeRequestContext {
    /**
     * 当前登录用户 uid 默认访客用户
     */
    private String currentLoginUid = "U00000000";
    /**
     * 当前登录用 token 默认访客用户
     */
    private String currentLoginToken = "0000";
    /**
     * 当前 secretKey
     */
    private String secretKey;
    /**
     * 访问渠道
     */
    private UserTokenScopeEnum scope;
    /**
     * 当前登录用户信息
     */
    private User currentLoginUser;

    public String getCurrentLoginUid() {
        return currentLoginUid;
    }

    public void setCurrentLoginUid(String currentLoginUid) {
        this.currentLoginUid = currentLoginUid;
    }

    public String getCurrentLoginToken() {
        return currentLoginToken;
    }

    public void setCurrentLoginToken(String currentLoginToken) {
        this.currentLoginToken = currentLoginToken;
    }

    public UserTokenScopeEnum getScope() {
        return scope;
    }

    public void setScope(UserTokenScopeEnum scope) {
        this.scope = scope;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public User getCurrentLoginUser() {
        return currentLoginUser;
    }

    public void setCurrentLoginUser(User currentLoginUser) {
        this.currentLoginUser = currentLoginUser;
    }
}