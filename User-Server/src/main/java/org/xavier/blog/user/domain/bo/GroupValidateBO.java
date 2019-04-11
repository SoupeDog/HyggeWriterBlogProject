package org.xavier.blog.user.domain.bo;

import org.xavier.common.utils.PropertiesHelper;
import org.xavier.common.utils.UtilsCreator;

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
    private String uId;
    /**
     * 群组唯一标识
     */
    private String gId;

    public String getuId() {
        return uId;
    }

    public void setuId(String uId) {
        this.uId = uId;
    }

    public String getgId() {
        return gId;
    }

    public void setgId(String gId) {
        this.gId = gId;
    }

    public void validate() {
        PropertiesHelper propertiesHelper = UtilsCreator.getInstance_DefaultPropertiesHelper();
        propertiesHelper.stringNotNull(uId, 9, 32, "Length of [uId] should within 9~32");
        propertiesHelper.stringNotNull(gId, 32, 32, "Length of [gId] should be 32");
    }
}