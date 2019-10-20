package org.xavier.blog.common;

/**
 * 描述信息：<br/>
 * 对外返回的网关对象
 *
 * @author Xavier
 * @version 1.0
 * @date 19-10-19
 * @since Jdk 1.8
 */
public class GatewayResponse {
    /**
     * 请求状态 1 成功  2 失败 3 服务端异常
     */
    private Integer state;
    /**
     * 自定义错误码
     */
    private Integer code;
    /**
     * 提示信息
     */
    private String msg;
    /**
     * 返回结果
     */
    private Object data;
    /**
     * 响应时间戳
     */
    private Long ts;

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public Long getTs() {
        return ts;
    }

    public void setTs(Long ts) {
        this.ts = ts;
    }

    public static class GatewayResponseBuilder {
        public static GatewayResponse buildSuccessGatewayResponse(Object data) {
            GatewayResponse result = new GatewayResponse();
            result.setCode(200);
            result.setState(1);
            result.setData(data);
            result.setTs(System.currentTimeMillis());
            return result;
        }

        public static GatewayResponse buildFailGatewayResponse(Integer state, Number code, String msg) {
            GatewayResponse result = new GatewayResponse();
            result.setCode(code.intValue());
            result.setState(state);
            result.setMsg(msg);
            result.setTs(System.currentTimeMillis());
            return result;
        }
    }
}