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
    WEB((byte) 1, "WEB"),
    ANDROID((byte) 2, "ANDROID"),
    IOS((byte) 3, "IOS");
    private Byte index;
    private String description;

    UserTokenScopeEnum(Byte index, String description) {
        this.index = index;
        this.description = description;
    }

    public Byte getIndex() {
        return index;
    }

    public void setIndex(Byte authority) {
        this.index = authority;
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
        switch (scope.toUpperCase()) {
            case "WEB":
                return UserTokenScopeEnum.WEB;
            case "ANDROID":
                return UserTokenScopeEnum.ANDROID;
            case "IOS":
                return UserTokenScopeEnum.IOS;
            default:
                throw new IllegalArgumentException("Unexpected authority of UserTokenScopeEnum.");
        }
    }
}
