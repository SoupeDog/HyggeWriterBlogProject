package org.xavier.blog.domain.bo;


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
    private Integer gid;

    private String gno;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
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

    public void validate() {
        PropertiesHelper propertiesHelper = UtilsCreator.getDefaultPropertiesHelperInstance();
        propertiesHelper.stringNotNull(uid, 9, 10, "[uid] can't be null,and its length should be between 9~10.");
        propertiesHelper.stringNotNull(gid, 32, 32, "Length of [gid] should be 32");
    }
}