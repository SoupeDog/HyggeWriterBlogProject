package org.xavier.blog.domain.bo;

import org.xavier.blog.common.enums.UserTokenScopeEnum;
import org.xavier.common.exception.Universal400Exception;
import org.xavier.common.util.PropertiesHelper;
import org.xavier.common.util.UtilsCreator;

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
    private String uid;
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
        PropertiesHelper propertiesHelper = UtilsCreator.getDefaultPropertiesHelperInstance();
        propertiesHelper.stringNotNull(uid, 9, 10, "[uid] can't be null,and its length should be between 9~10.");
        propertiesHelper.stringNotNull(token, "[token] can't be null or empty.");
    }

    public UserTokenScopeEnum calculateScope() throws Universal400Exception {
        if (scope == null || "".equals(scope.trim())) {
            if (scopeByte == null) {
                throw new Universal400Exception("[scopeByte]、[scope] can't be null at the same time.");
            } else {
                return UserTokenScopeEnum.getUserTypeEnum(scopeByte);
            }
        } else {
            return UserTokenScopeEnum.getUserTypeEnum(scope);
        }
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
