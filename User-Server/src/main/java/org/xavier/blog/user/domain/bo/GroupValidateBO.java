package org.xavier.blog.user.domain.bo;


import org.xavier.common.util.PropertiesHelper;
import org.xavier.common.util.UtilsCreator;

/**
 * 描述信息：<br/>
 * 群组校验业务对象
 *
 * @author Xavier
 * @version 1.0
 * @date 2019/4/11
 * @since Jdk 1.8
 */
public class GroupValidateBO {

    /**
     * 用户唯一标识
     */
    private String uid;
    /**
     * 群组唯一标识
     */
    private String gid;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getGid() {
        return gid;
    }

    public void setGid(String gid) {
        this.gid = gid;
    }

    public void validate() {
        PropertiesHelper propertiesHelper = UtilsCreator.getDefaultPropertiesHelperInstance();
        propertiesHelper.stringNotNull(uid, 9, 10, "[uid] can't be null,and its length should be between 9~10.");
        propertiesHelper.stringNotNull(gid, 32, 32, "Length of [gid] should be 32");
    }
}