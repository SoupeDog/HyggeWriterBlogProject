package org.xavier.blog.user.domain.po.group;

import org.xavier.common.util.PropertiesHelper;
import org.xavier.common.util.UtilsCreator;

/**
 * 描述信息：<br/>
 * 群组
 *
 * @author Xavier
 * @version 1.0
 * @date 2019/4/5
 * @since Jdk 1.8
 */
public class Group {
    /**
     * 组唯一标识
     */
    private Integer gid;
    /**
     * 组编号
     */
    private String gno;
    /**
     * 群主
     */
    private String groupOwner;
    /**
     * 组名
     */
    private String groupName;
    /**
     * 最后修改时间 utc 毫秒级时间戳
     */
    private Long lastUpdateTs;
    /**
     * 创建时间 utc 毫秒级时间戳
     */
    private Long createTs;

    public void validate() {
        PropertiesHelper propertiesHelper = UtilsCreator.getDefaultPropertiesHelperInstance();
        propertiesHelper.stringNotNull(groupOwner, 9, 10, "[groupOwner] can't be null,and its length should within 9~10.");
        propertiesHelper.stringNotNull(groupName, 1, 32, "[uName] can't be null,and its length should within 32.");
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

    public String getGroupOwner() {
        return groupOwner;
    }

    public void setGroupOwner(String groupOwner) {
        this.groupOwner = groupOwner;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public Long getLastUpdateTs() {
        return lastUpdateTs;
    }

    public void setLastUpdateTs(Long lastUpdateTs) {
        this.lastUpdateTs = lastUpdateTs;
    }

    public Long getCreateTs() {
        return createTs;
    }

    public void setCreateTs(Long createTs) {
        this.createTs = createTs;
    }
}