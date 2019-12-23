package org.xavier.blog.domain.po;

import org.xavier.common.util.PropertiesHelper;
import org.xavier.common.util.UtilsCreator;

/**
 * 描述信息：<br/>
 * 群组加入记录
 *
 * @author Xavier
 * @version 1.0
 * @date 2019/4/5
 * @since Jdk 1.8
 */
public class GroupRelationship {
    /**
     * 自增 grid
     */
    private Integer grid;
    /**
     * 群组唯一标识
     */
    private Integer gid;
    private String gno;
    /**
     * 用户唯一标识
     */
    private String uid;
    /**
     * 创建时间 utc 毫秒级时间戳
     */
    private Long createTs;

    public Integer getGrid() {
        return grid;
    }

    public void setGrid(Integer grid) {
        this.grid = grid;
    }

    public Integer getGid() {
        return gid;
    }

    public void setGid(Integer gid) {
        this.gid = gid;
    }

    public String getGno() {
        return gno;
    }

    public void setGno(String gno) {
        this.gno = gno;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public Long getCreateTs() {
        return createTs;
    }

    public void setCreateTs(Long createTs) {
        this.createTs = createTs;
    }

    public void validate() {
        PropertiesHelper propertiesHelper = UtilsCreator.getDefaultPropertiesHelperInstance();
        propertiesHelper.stringNotNull(uid, 1, 32, "[uid] can't be null,and its length should be between 9~10.");
        propertiesHelper.stringNotNull(gno, 0, 32, "[gno] can't be null,and its length should be 32.");
    }
}