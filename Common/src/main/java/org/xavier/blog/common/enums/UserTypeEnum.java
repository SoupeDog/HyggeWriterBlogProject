package org.xavier.blog.common.enums;

import org.xavier.common.exception.PropertiesRuntimeException;

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
    ROOT((byte) 1, "超级管理员"),
    NORMAL((byte) 2, "普通用户");
    private Byte index;
    private String description;

    UserTypeEnum(Byte index, String description) {
        this.index = index;
        this.description = description;
    }

    public Byte getIndex() {
        return index;
    }

    public void setIndex(Byte index) {
        this.index = index;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public static UserTypeEnum getUserTypeEnum(Number authority) {
        switch (authority.byteValue()) {
            case (byte) 1:
                return UserTypeEnum.ROOT;
            case (byte) 2:
                return UserTypeEnum.NORMAL;
            default:
                throw new PropertiesRuntimeException("Unexpected authority of UserTypeEnum.");
        }
    }

    public static UserTypeEnum getUserTypeEnum(String authority) {
        switch (authority.toUpperCase()) {
            case "ROOT":
                return UserTypeEnum.ROOT;
            case "NORMAL":
                return UserTypeEnum.NORMAL;
            default:
                throw new PropertiesRuntimeException("Unexpected authority of UserTypeEnum.");
        }
    }
}
