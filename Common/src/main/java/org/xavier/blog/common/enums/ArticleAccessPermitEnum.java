package org.xavier.blog.common.enums;

/**
 * 描述信息：<br/>
 * 文章访问许可类型枚举
 *
 * @author Xavier
 * @version 1.0
 * @date 2018/3/7
 * @since Jdk 1.8
 */
public enum ArticleAccessPermitEnum {
    PERSONAL((byte) 0, "仅自己可见"),
    SECRET_KEY((byte) 1, "秘钥访问"),
    GROUP((byte) 2, "特定群组可见"),
    MALE((byte) 3, "仅男性可见"),
    FEMALE((byte) 4, "仅女性可见"),
    CRON((byte) 5, "周期性可见"),
    PUBLIC((byte) 6, "公开可见");

    private Byte index;
    private String description;

    ArticleAccessPermitEnum(Byte index, String description) {
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

    public static ArticleAccessPermitEnum getArticleAccessPermitEnum(Number number) {
        switch (number.byteValue()) {
            case (byte) 0:
                return ArticleAccessPermitEnum.PERSONAL;
            case (byte) 1:
                return ArticleAccessPermitEnum.SECRET_KEY;
            case (byte) 2:
                return ArticleAccessPermitEnum.GROUP;
            case (byte) 3:
                return ArticleAccessPermitEnum.MALE;
            case (byte) 4:
                return ArticleAccessPermitEnum.FEMALE;
            case (byte) 5:
                return ArticleAccessPermitEnum.CRON;
            case (byte) 6:
                return ArticleAccessPermitEnum.PUBLIC;
            default:
                throw new IllegalArgumentException("Unexpected index of ArticleAccessPermitEnum.");
        }
    }
}