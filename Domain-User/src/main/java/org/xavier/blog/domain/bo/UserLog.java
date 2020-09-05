package org.xavier.blog.domain.bo;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.xavier.blog.common.enums.UserActionEnum;
import org.xavier.common.util.UtilsCreator;

import java.util.LinkedHashMap;

/**
 * 描述信息：<br/>
 *
 * @author Xavier
 * @version 1.0
 * @date 2020/9/5
 * @since Jdk 1.8
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserLog {
    private String ip;
    private String uid;
    /**
     * 行为
     */
    private UserActionEnum action;
    /**
     * 更多信息
     */
    private Object msg;
    /**
     * 发生时间
     */
    private Long ts;

    public UserLog(String ip, String uid, UserActionEnum action, Object... msgs) {
        this.ip = ip;
        this.uid = uid;
        this.action = action;
        if (msgs == null) {
            this.msg = "无";
        } else if (msgs.length == 1) {
            this.msg = msgs[0];
            if (this.msg instanceof String && ((String) this.msg).trim().length() < 1) {
                this.msg = "空";
            }
        } else {
            LinkedHashMap finalObj = new LinkedHashMap(msgs.length);
            int count = 1;
            for (Object o : msgs) {
                finalObj.put(String.format("信息 %d", count), o);
                count = count + 1;
            }
            this.msg = finalObj;
        }
        this.ts = System.currentTimeMillis();
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public UserActionEnum getAction() {
        return action;
    }

    public void setAction(UserActionEnum action) {
        this.action = action;
    }

    public Object getMsg() {
        return msg;
    }

    public void setMsg(Object msg) {
        this.msg = msg;
    }

    public Long getTs() {
        return ts;
    }

    public void setTs(Long ts) {
        this.ts = ts;
    }

    @Override
    public String toString() {
        return UtilsCreator.getDefaultJsonHelperInstance(false).format(this);
    }
}