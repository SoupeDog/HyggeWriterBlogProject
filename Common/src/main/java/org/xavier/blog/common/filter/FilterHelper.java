package org.xavier.blog.common.filter;

import org.apache.tomcat.util.http.MimeHeaders;
import org.xavier.common.exception.Universal500RuntimeException;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;

/**
 * 过滤器工具
 */
public class FilterHelper {

    /**
     * RequestFacade 的成员变量 request 声明
     */
    private static final Field requestFieldOfRequestFacade;
    /**
     * org.apache.catalina.connector.Request 的成员变量 coyoteRequest 声明
     */
    private static final Field coyoteRequestOfRequest;
    /**
     * org.apache.coyote.Request 的成员变量 headers 声明
     */
    private static final Field mimeHeadersOfCoyoteRequest;

    static {
        try {
            requestFieldOfRequestFacade = org.apache.catalina.connector.RequestFacade.class.getDeclaredField("request");
            requestFieldOfRequestFacade.setAccessible(true);
            coyoteRequestOfRequest = org.apache.catalina.connector.Request.class.getDeclaredField("coyoteRequest");
            coyoteRequestOfRequest.setAccessible(true);
            mimeHeadersOfCoyoteRequest = org.apache.coyote.Request.class.getDeclaredField("headers");
            mimeHeadersOfCoyoteRequest.setAccessible(true);
        } catch (NoSuchFieldException e) {
            throw new Universal500RuntimeException("Fail to init addValueToHeaders().");
        }
    }

    /**
     * 反射获取 Headers 的控制权并为其添加属性
     */
    public static void addValueToHeaders(HttpServletRequest rowRequest, String key, String value) {
        try {
            // 获取 RequestFacade 对象中获取 Request 类型成员变量
            org.apache.catalina.connector.Request request = (org.apache.catalina.connector.Request) requestFieldOfRequestFacade.get(rowRequest);
            // Request 对象中获取 org.apache.coyote.Request 类型成员变量
            org.apache.coyote.Request coyoteRequest = (org.apache.coyote.Request) coyoteRequestOfRequest.get(request);
            // org.apache.coyote.Request 对象中获取 MimeHeaders 类型成员变量
            MimeHeaders mimeHeaders = (MimeHeaders) mimeHeadersOfCoyoteRequest.get(coyoteRequest);
            // 设置 headers 属性
            mimeHeaders.addValue(key).setString(value);
        } catch (Exception e) {
            throw new Universal500RuntimeException("Fail to addValueToHeaders.[key]:" + key + " [value]:" + value);
        }
    }
}
