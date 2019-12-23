package org.xavier.blog.domain.po;

import org.xavier.common.util.PropertiesHelper;
import org.xavier.common.util.UtilsCreator;

/**
 * 用户操作日志实体
 */
public class UserOperationLog {
    /**
     * 日志唯一标识
     */
    private Integer id;

    /**
     * 用户唯一标识
     */
    private String uId;

    /**
     * 用户行为
     */
    private String behavior;

    /**
     * 要点信息
     */
    private String mainPoints;

    /**
     * 请求方 ip(到达个人站时的 Ip)
     */
    private String realIp;

    /**
     * 请求方代理设备信息
     */
    private String userAgent;

    /**
     * 操作时间
     */
    private Long ts;

    public void validate() {
        PropertiesHelper propertiesHelper = UtilsCreator.getDefaultPropertiesHelperInstance();
        propertiesHelper.stringNotNull(uId, 9, 10, "[uId] can't be null,and its length should be between 9~10.");
        propertiesHelper.stringNotNull(behavior, 0, 100, "[behavior] can't be null,and its length should be between 0~100.");
        propertiesHelper.stringNotNull(mainPoints, 0, 200, "[mainPoints] can't be null,and its length should be between 0~200.");
        propertiesHelper.stringNotNull(realIp, 0, 32, "[realIp] can't be null,and its length should be between 0~32.");
        propertiesHelper.stringNotNull(userAgent, 0, 500, "[userAgent] can't be null,and its length should be between 0~500.");
        propertiesHelper.longRangeNotNull(ts, "[ts] can't be null,and it should be a long number.");
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getuId() {
        return uId;
    }

    public void setuId(String uId) {
        this.uId = uId;
    }

    public String getBehavior() {
        return behavior;
    }

    public void setBehavior(String behavior) {
        this.behavior = behavior;
    }

    public String getMainPoints() {
        return mainPoints;
    }

    public void setMainPoints(String mainPoints) {
        this.mainPoints = mainPoints;
    }

    public String getRealIp() {
        return realIp;
    }

    public void setRealIp(String realIp) {
        this.realIp = realIp;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public Long getTs() {
        return ts;
    }

    public void setTs(Long ts) {
        this.ts = ts;
    }
}