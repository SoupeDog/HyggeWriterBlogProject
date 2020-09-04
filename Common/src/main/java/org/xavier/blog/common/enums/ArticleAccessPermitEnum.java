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
    PERSONAL((byte) 1, "仅自己可见"),
    SECRET_KEY((byte) 2, "秘钥访问"),
    GROUP((byte) 3, "特定群组可见"),
    MALE((byte) 4, "仅男性可见"),
    FEMALE((byte) 5, "仅女性可见"),
    CRON((byte) 6, "周期性可见"),
    PUBLIC((byte) 7, "公开可见");

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
            case (byte) 1:
                return ArticleAccessPermitEnum.PERSONAL;
            case (byte) 2:
                return ArticleAccessPermitEnum.SECRET_KEY;
            case (byte) 3:
                return ArticleAccessPermitEnum.GROUP;
            case (byte) 4:
                return ArticleAccessPermitEnum.MALE;
            case (byte) 5:
                return ArticleAccessPermitEnum.FEMALE;
            case (byte) 6:
                return ArticleAccessPermitEnum.CRON;
            case (byte) 7:
                return ArticleAccessPermitEnum.PUBLIC;
            default:
                throw new IllegalArgumentException("Unexpected index of ArticleAccessPermitEnum.");
        }
    }
}