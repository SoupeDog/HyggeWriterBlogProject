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
    private String gId;
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
    private Long ts;

    public void validate(){
       PropertiesHelper propertiesHelper= UtilsCreator.getDefaultPropertiesHelperInstance();
       propertiesHelper.stringNotNull(groupOwner,9,10,"[groupOwner] can't be null,and its length should within 9~10.");
       propertiesHelper.stringNotNull(groupName, 1, 32, "[uName] can't be null,and its length should within 32.");
    }

    public String getgId() {
        return gId;
    }

    public void setgId(String gId) {
        this.gId = gId;
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

    public Long getTs() {
        return ts;
    }

    public void setTs(Long ts) {
        this.ts = ts;
    }
}