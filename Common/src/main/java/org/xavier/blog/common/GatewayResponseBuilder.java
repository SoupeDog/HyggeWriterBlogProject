package org.xavier.blog.common;

/**
 * 描述信息：<br/>
 *
 * @author Xavier
 * @version 1.0
 * @date 19-12-23
 * @since Jdk 1.8
 */
public class GatewayResponseBuilder {
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