package org.xavier.blog.user.filter;

import org.apache.tomcat.util.http.MimeHeaders;
import org.springframework.web.filter.OncePerRequestFilter;
import org.xavier.blog.user.domain.enums.UserTokenScopeEnum;
import org.xavier.blog.user.service.UserTokenServiceImpl;
import org.xavier.common.exception.Universal_500_X_Exception_Runtime;
import org.xavier.common.exception.base.RequestException;
import org.xavier.common.exception.base.RequestException_Runtime;
import org.xavier.common.exception.base.ServiceException_Runtime;
import org.xavier.common.utils.PropertiesHelper;
import org.xavier.common.utils.UtilsCreator;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Field;

/**
 * 描述信息：<br/>
 *
 * @author Xavier
 * @version 1.0
 * @date 2018/7/4
 * @since Jdk 1.8
 */
public class LoginFilter extends OncePerRequestFilter {
    private UserTokenServiceImpl userTokenService;

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
            throw new Universal_500_X_Exception_Runtime("Fail to init addValueToHeaders().");
        }
    }

    public LoginFilter() {
    }

    public LoginFilter(UserTokenServiceImpl userTokenService) {
        this.userTokenService = userTokenService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) {
        PropertiesHelper propertiesHelper = UtilsCreator.getInstance_DefaultPropertiesHelper();
        String uId, token;
        UserTokenScopeEnum scope;
        try {
            switch (request.getMethod().toUpperCase()) {
                case "GET":
                case "POST":
                case "PUT":
                case "DELETE":
                    uId = propertiesHelper.string(request.getHeader("uId"));
                    token = propertiesHelper.string(request.getHeader("token"));
                    if (uId == null && token == null) {
                        addValueToHeaders(request, "uId", "U00000000");
                    } else {
                        scope = UserTokenScopeEnum.getUserTypeEnum(propertiesHelper.stringNotNull(request.getHeader("scope"), "[scope] can't be null."));
                        userTokenService.validateUserToken(uId, token, scope);
                    }
                    filterChain.doFilter(request, response);
                    break;
                default:
            }
        } catch (RequestException_Runtime e) {
            onError(response, e);
        } catch (ServiceException_Runtime e) {
            onError(response, e);
        } catch (ServletException e) {
            onError(response, e);
        } catch (IOException e) {
            onError(response, e);
        } catch (RequestException e) {
            onError(response, e);
        }
    }

    private void onError(HttpServletResponse response, RequestException_Runtime e) {
        initResponse(response, e.getStateCode(), e.getMessage());
    }

    /**
     * 反射获取 Headers 的控制权并为其添加属性
     */
    private void addValueToHeaders(HttpServletRequest rowRequest, String key, String value) {
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
            throw new Universal_500_X_Exception_Runtime("Fail to addValueToHeaders.[key]:" + key + " [value]:" + value);
        }
    }

    private void onError(HttpServletResponse response, ServiceException_Runtime e) {
        initResponse(response, e.getStateCode(), e.getMessage());
    }

    private void onError(HttpServletResponse response, RequestException e) {
        initResponse(response, e.getStateCode(), e.getMessage());
    }

    private void onError(HttpServletResponse response, ServletException e) {
        initResponse(response, 500F, e.getMessage());
    }

    private void onError(HttpServletResponse response, IOException e) {
        initResponse(response, 400F, e.getMessage());
    }

    private void initResponse(HttpServletResponse response, Float stateCode, String message) {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        response.setStatus(stateCode.intValue());
        PrintWriter out = null;
        try {
            out = response.getWriter();
            out.append("{\n" +
                    "    \"type\": 2,\n" +
                    "    \"code\": " + stateCode + ",\n" +
                    "    \"msg\": \"" + message + "\",\n" +
                    "    \"data\": null,\n" +
                    "    \"ts\": " + System.currentTimeMillis() + "\n" +
                    "}");
        } catch (IOException e2) {
            e2.printStackTrace();
        } finally {
            if (out != null) {
                out.close();
            }
        }
    }
}
