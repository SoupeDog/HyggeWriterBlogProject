package org.xavier.blog.common.enums;

/**
 * 描述信息：<br/>
 * 用户类型
 *
 * @author Xavier
 * @version 1.0
 * @date 2018.05.08
 * @since Jdk 1.8
 */
public enum UserTypeEnum {
    ROOT((byte) 0, "超级管理员"),
    WRITER((byte) 1, "作者"),
    READER((byte) 2, "读者");
    private Byte authority;
    private String description;

    UserTypeEnum(Byte authority, String description) {
        this.authority = authority;
        this.description = description;
    }

    public Byte getAuthority() {
        return authority;
    }

    public void setAuthority(Byte authority) {
        this.authority = authority;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public static UserTypeEnum getUserTypeEnum(Number authority) {
        switch (authority.byteValue()) {
            case (byte) 0:
                return UserTypeEnum.ROOT;
            case (byte) 1:
                return UserTypeEnum.WRITER;
            case (byte) 2:
                return UserTypeEnum.READER;
            default:
                throw new IllegalArgumentException("Unexpected authority of UserTypeEnum.");
        }
    }
}
