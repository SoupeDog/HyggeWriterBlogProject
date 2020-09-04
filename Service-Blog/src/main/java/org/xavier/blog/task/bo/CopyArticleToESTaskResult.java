package org.xavier.blog.task.bo;

import java.util.ArrayList;

/**
 * 描述信息：<br/>
 *
 * @author Xavier
 * @version 1.0
 * @date 2020/9/4
 * @since Jdk 1.8
 */
public class CopyArticleToESTaskResult {
    private Long startTs;
    private Long endTs;
    private Integer totalArticleCount;
    private Integer successCount;
    private Long cost;
    private ArrayList<String> failArticleNo = new ArrayList<>();

    public Long getStartTs() {
        return startTs;
    }

    public void setStartTs(Long startTs) {
        this.startTs = startTs;
    }

    public Long getEndTs() {
        return endTs;
    }

    public void setEndTs(Long endTs) {
        this.endTs = endTs;
    }

    public Integer getTotalArticleCount() {
        return totalArticleCount;
    }

    public void setTotalArticleCount(Integer totalArticleCount) {
        this.totalArticleCount = totalArticleCount;
    }

    public Integer getSuccessCount() {
        return successCount;
    }

    public void setSuccessCount(Integer successCount) {
        this.successCount = successCount;
    }

    public ArrayList<String> getFailArticleNo() {
        return failArticleNo;
    }

    public void setFailArticleNo(ArrayList<String> failArticleNo) {
        this.failArticleNo = failArticleNo;
    }

    public Long getCost() {
        return endTs - startTs;
    }
}