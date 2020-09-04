package org.xavier.blog.common.enums;

/**
 * 描述信息：<br/>
 * 用户 token 作用域类型
 *
 * @author Xavier
 * @version 1.0
 * @date 2018.05.08
 * @since Jdk 1.8
 */
public enum UserTokenScopeEnum {
    WEB((byte) 1, "web"),
    ANDROID((byte) 2, "android"),
    IOS((byte) 3, "ios");
    private Byte scope;
    private String description;

    UserTokenScopeEnum(Byte scope, String description) {
        this.scope = scope;
        this.description = description;
    }

    public Byte getScope() {
        return scope;
    }

    public void setScope(Byte authority) {
        this.scope = authority;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public static UserTokenScopeEnum getUserTypeEnum(Number scope) {
        switch (scope.byteValue()) {
            case (byte) 1:
                return UserTokenScopeEnum.WEB;
            case (byte) 2:
                return UserTokenScopeEnum.ANDROID;
            case (byte) 3:
                return UserTokenScopeEnum.IOS;
            default:
                throw new IllegalArgumentException("Unexpected authority of UserTokenScopeEnum.");
        }
    }

    public static UserTokenScopeEnum getUserTypeEnum(String scope) {
        switch (scope.toLowerCase()) {
            case "web":
                return UserTokenScopeEnum.WEB;
            case "android":
                return UserTokenScopeEnum.ANDROID;
            case "ios":
                return UserTokenScopeEnum.IOS;
            default:
                throw new IllegalArgumentException("Unexpected authority of UserTokenScopeEnum.");
        }
    }
}
