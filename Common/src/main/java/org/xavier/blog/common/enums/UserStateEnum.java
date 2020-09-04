package org.xavier.blog.common.enums;

import org.xavier.common.exception.PropertiesRuntimeException;

/**
 * 描述信息：<br/>
 * 用户状态
 *
 * @author Xavier
 * @version 1.0
 * @date 2018.05.08
 * @since Jdk 1.8
 */
public enum UserStateEnum {
    /**
     * 冻结的
     */
    FROZEN((byte) 1, "FROZEN"),
    /**
     * 激活的
     */
    ACTIVE((byte) 2, "ACTIVE");
    private Byte index;
    private String description;

    UserStateEnum(Byte index, String description) {
        this.index = index;
        this.description = description;
    }

    public Byte getIndex() {
        return index;
    }

    public String getDescription() {
        return description;
    }

    public static UserStateEnum getUserStateEnum(Number userState) {
        switch (userState.byteValue()) {
            case (byte) 1:
                return UserStateEnum.FROZEN;
            case (byte) 2:
                return UserStateEnum.ACTIVE;
            default:
                throw new PropertiesRuntimeException("Unexpected userState of UserStateEnum.");
        }
    }

    public static UserStateEnum getUserStateEnum(String userState) {
        switch (userState.toUpperCase()) {
            case "FROZEN":
                return UserStateEnum.FROZEN;
            case "ACTIVE":
                return UserStateEnum.ACTIVE;
            default:
                throw new PropertiesRuntimeException("Unexpected userState of UserStateEnum.");
        }
    }
}
