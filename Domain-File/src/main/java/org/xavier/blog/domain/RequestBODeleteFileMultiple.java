package org.xavier.blog.domain;

import java.util.ArrayList;

/**
 * 描述信息：<br/>
 *
 * @author Xavier
 * @version 1.0
 * @date 19-12-5
 * @since Jdk 1.8
 */
public class RequestBODeleteFileMultiple {
    private ArrayList<String> fileNameList;

    public ArrayList<String> getFileNameList() {
        return fileNameList;
    }

    public void setFileNameList(ArrayList<String> fileNameList) {
        this.fileNameList = fileNameList;
    }
}
