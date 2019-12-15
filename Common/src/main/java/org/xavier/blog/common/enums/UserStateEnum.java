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
    FROZEN((byte) 0, "冻结的"),
    ACTIVE((byte) 1, "激活的");
    private Byte userState;
    private String description;

    UserStateEnum(Byte userState, String description) {
        this.userState = userState;
        this.description = description;
    }

    public Byte getUserState() {
        return userState;
    }

    public String getDescription() {
        return description;
    }

    public static UserStateEnum getUserStateEnum(Number userState) {
        switch (userState.byteValue()) {
            case (byte) 0:
                return UserStateEnum.FROZEN;
            case (byte) 1:
                return UserStateEnum.ACTIVE;
            default:
                throw new PropertiesRuntimeException("Unexpected userState of UserStateEnum.");
        }
    }
}
