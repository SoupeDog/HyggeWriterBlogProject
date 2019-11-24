package org.xavier.blog.common.enums;

/**
 * 描述信息：<br/>
 * 文章存储类型枚举
 *
 * @author Xavier
 * @version 1.0
 * @date 2018.01.12
 * @since Jdk 1.8
 */
public enum ArticleRetainTypeEnum {
    DEFAULT((byte) 0, "默认文章，仅保存文章内容"),
    HTML((byte) 1, "Html 类型文章，本身为 html 文件");
    private Byte value;
    private String description;

    ArticleRetainTypeEnum(Byte value, String description) {
        this.value = value;
        this.description = description;
    }

    public Byte getValue() {
        return value;
    }

    public void setValue(Byte value) {
        this.value = value;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public static ArticleRetainTypeEnum getArticleRetainTypeEnum(Number number) {
        switch (number.byteValue()) {
            case (byte) 0:
                return ArticleRetainTypeEnum.DEFAULT;
            case (byte) 1:
                return ArticleRetainTypeEnum.HTML;
            default:
                throw new IllegalArgumentException("Unexpected index of ArticleRetainTypeEnum.");
        }
    }
}
