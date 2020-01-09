package org.xavier.blog.domain;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * 描述信息：<br/>
 *
 * @author Xavier
 * @version 1.0
 * @date 19-12-7
 * @since Jdk 1.8
 */
public class FileInfo {
    /**
     * byte → mb 进制
     */
    public static final BigDecimal byteToMb = new BigDecimal(1048576);

    private String fileName;
    /**
     * 文件大小 单位 MB
     */
    private BigDecimal fileSize;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public BigDecimal getFileSize() {
        return fileSize;
    }

    public void setFileSize(BigDecimal fileSize) {
        this.fileSize = fileSize.setScale(2, RoundingMode.FLOOR);
    }
}
