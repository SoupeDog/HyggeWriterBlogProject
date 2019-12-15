package org.xavier.blog.common.enums;

/**
 * 描述信息：<br/>
 * 用户性别类型
 *
 * @author Xavier
 * @version 1.0
 * @date 2018.05.08
 * @since Jdk 1.8
 */
public enum UserSexEnum {
    SECRET((byte) 0, "秘"),
    MALE((byte) 1, "男"),
    FEMALE((byte) 2, "女");
    private Byte index;
    private String description;

    UserSexEnum(Byte index, String description) {
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

    public static UserSexEnum getUserSexEnum(Number index) {
        switch (index.byteValue()) {
            case (byte) 0:
                return UserSexEnum.SECRET;
            case (byte) 1:
                return UserSexEnum.MALE;
            case (byte) 2:
                return UserSexEnum.FEMALE;
            default:
                throw new IllegalArgumentException("Unexpected index of UserSexEnum.");
        }
    }



    public static UserSexEnum getUserSexEnum(String description) {
        switch (description) {
            case "秘":
                return UserSexEnum.SECRET;
            case "男":
                return UserSexEnum.MALE;
            case "女":
                return UserSexEnum.FEMALE;
            default:
                throw new IllegalArgumentException("Unexpected index of UserSexEnum.");
        }
    }
}
