package org.xavier.blog.user.domain.dto.group;

/**
 * 描述信息：<br/>
 * 群组加入记录
 *
 * @author Xavier
 * @version 1.0
 * @date 2019/4/5
 * @since Jdk 1.8
 */
public class GroupJoinRecordDTO {
    /**
     * 群组唯一标识
     */
    private String gId;
    /**
     * 用户唯一标识
     */
    private String uId;
    /**
     * 创建时间 utc 毫秒级时间戳
     */
    private Long ts;

    public String getgId() {
        return gId;
    }

    public void setgId(String gId) {
        this.gId = gId;
    }

    public String getUId() {
        return uId;
    }

    public void setUId(String uId) {
        this.uId = uId;
    }

    public Long getTs() {
        return ts;
    }

    public void setTs(Long ts) {
        this.ts = ts;
    }
}