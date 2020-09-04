package org.xavier.blog.common.enums;

/**
 * 描述信息：<br/>
 *
 * @author Xavier
 * @version 1.0
 * @date 19-12-29
 * @since Jdk 1.8
 */
public enum DefaultStateEnum {
    INACTIVE((byte) 1, "禁用"),
    ACTIVE((byte) 2, "启用");
    private Byte index;
    private String description;

    DefaultStateEnum(Byte index, String description) {
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

    public static DefaultStateEnum getDefaultStateEnum(Number index) {
        switch (index.byteValue()) {
            case (byte) 1:
                return DefaultStateEnum.INACTIVE;
            case (byte) 2:
                return DefaultStateEnum.ACTIVE;
            default:
                throw new IllegalArgumentException("Unexpected index of DefaultStateEnum.");
        }
    }

    public static DefaultStateEnum getDefaultStateEnum(String description) {
        switch (description) {
            case "禁用":
                return  DefaultStateEnum.INACTIVE;
            case "启用":
                return DefaultStateEnum.ACTIVE;
            default:
                throw new IllegalArgumentException("Unexpected index of DefaultStateEnum.");
        }
    }
}
