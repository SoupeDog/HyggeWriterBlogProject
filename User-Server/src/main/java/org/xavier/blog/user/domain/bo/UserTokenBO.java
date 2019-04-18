package org.xavier.blog.user.domain.bo;

import org.xavier.blog.user.domain.enums.UserTokenScopeEnum;
import org.xavier.common.exception.Universal_400_X_Exception;
import org.xavier.common.utils.PropertiesHelper;
import org.xavier.common.utils.UtilsCreator;

/**
 * 描述信息：<br/>
 * 用户 Token 业务对象
 *
 * @author Xavier
 * @version 1.0
 * @date 2018/7/3
 * @since Jdk 1.8
 */
public class UserTokenBO {
    /**
     * 用户唯一标识
     */
    private String uId;
    /**
     * 用户 token
     */
    private String token;
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

    public void validate() {
        PropertiesHelper propertiesHelper = UtilsCreator.getInstance_DefaultPropertiesHelper();
        propertiesHelper.stringNotNull(uId, 9, 10, "[uId] can't be null,and its length should be between 9~10.");
        propertiesHelper.stringNotNull(token, 32, 32, "[token] can't be null,and its length should be 32.");
    }

    public UserTokenScopeEnum calculateScope() throws Universal_400_X_Exception {
        if (scope == null || scope.trim().equals("")) {
            if (scopeByte == null) {
                throw new Universal_400_X_Exception("[scopeByte]、[scope] can't be null at the same time.");
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

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
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
