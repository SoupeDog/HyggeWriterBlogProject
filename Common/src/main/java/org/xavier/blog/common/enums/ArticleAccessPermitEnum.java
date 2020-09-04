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
    /**
     * 仅自己可见
     */
    PERSONAL((byte) 1, "PERSONAL"),
    /**
     * 秘钥访问
     */
    SECRET_KEY((byte) 2, "SECRET_KEY"),
    /**
     * 特定群组可见
     */
    GROUP((byte) 3, "GROUP"),
    /**
     * 仅男性可见
     */
    MALE((byte) 4, "MALE"),
    /**
     * 仅女性可见
     */
    FEMALE((byte) 5, "FEMALE"),
    /**
     * 周期性可见
     */
    CRON((byte) 6, "CRON"),
    /**
     * 公开可见
     */
    PUBLIC((byte) 7, "PUBLIC");

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

    public static ArticleAccessPermitEnum getArticleAccessPermitEnum(String articleAccessPermit) {
        switch (articleAccessPermit.toUpperCase()) {
            case "PERSONAL":
                return ArticleAccessPermitEnum.PERSONAL;
            case "SECRET_KEY":
                return ArticleAccessPermitEnum.SECRET_KEY;
            case "GROUP":
                return ArticleAccessPermitEnum.GROUP;
            case "MALE":
                return ArticleAccessPermitEnum.MALE;
            case "FEMALE":
                return ArticleAccessPermitEnum.FEMALE;
            case "CRON":
                return ArticleAccessPermitEnum.CRON;
            case "PUBLIC":
                return ArticleAccessPermitEnum.PUBLIC;
            default:
                throw new IllegalArgumentException("Unexpected authority of UserTokenScopeEnum.");
        }
    }
}