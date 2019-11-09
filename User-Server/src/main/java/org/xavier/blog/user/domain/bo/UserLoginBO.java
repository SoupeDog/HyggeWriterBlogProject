package org.xavier.blog.user.domain.bo;

import org.xavier.blog.user.domain.enums.UserTokenScopeEnum;
import org.xavier.common.exception.Universal400Exception;

/**
 * 描述信息：<br/>
 * 用户登录业务对象
 *
 * @author Xavier
 * @version 1.0
 * @date 2018/7/3
 * @since Jdk 1.8
 */
public class UserLoginBO {
    /**
     * 用户唯一标识
     */
    private String uId;
    /**
     * 用户密码
     */
    private String pw;
    /**
     * 作用域 数字
     */
    private Byte scopeByte;
    /**
     * 作用域 字符串
     */
    private String scope;

    /**
     * token 刷新秘钥
     */
    private String refreshKey;

    public UserTokenScopeEnum calculateScope() throws Universal400Exception {
        if (scope == null || scope.trim().equals("")) {
            if (scopeByte == null) {
                throw new Universal400Exception("[scope]、[scopeByte] can't be null at the same time.");
            } else {
                return UserTokenScopeEnum.getUserTypeEnum(scopeByte);
            }
        } else {
            return UserTokenScopeEnum.getUserTypeEnum(scope);
        }
    }

    public String getuId() {
        return uId;
    }

    public void setuId(String uId) {
        this.uId = uId;
    }

    public String getPw() {
        return pw;
    }

    public void setPw(String pw) {
        this.pw = pw;
    }

    public Byte getScopeByte() {
        return scopeByte;
    }


    public void setScopeByte(Byte scopeByte) {
        this.scopeByte = scopeByte;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getRefreshKey() {
        return refreshKey;
    }

    public void setRefreshKey(String refreshKey) {
        this.refreshKey = refreshKey;
    }
}
