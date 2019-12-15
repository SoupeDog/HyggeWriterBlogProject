package org.xavier.blog.user.filter;

import org.springframework.web.filter.OncePerRequestFilter;
import org.xavier.blog.common.filter.FilterHelper;
import org.xavier.blog.common.enums.UserTokenScopeEnum;
import org.xavier.blog.user.service.UserTokenServiceImpl;
import org.xavier.common.exception.base.RequestException;
import org.xavier.common.exception.base.RequestRuntimeException;
import org.xavier.common.exception.base.ServiceRuntimeException;
import org.xavier.common.util.PropertiesHelper;
import org.xavier.common.util.UtilsCreator;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

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

    public LoginFilter() {
    }

    public LoginFilter(UserTokenServiceImpl userTokenService) {
        this.userTokenService = userTokenService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) {
        PropertiesHelper propertiesHelper = UtilsCreator.getDefaultPropertiesHelperInstance();
        String uId, token;
        UserTokenScopeEnum scope;
        try {
            switch (request.getMethod().toUpperCase()) {
                case "GET":
                case "POST":
                case "PUT":
                case "DELETE":
                    uId = propertiesHelper.stringOfNullable(request.getHeader("uId"), "U00000000");
                    token = propertiesHelper.stringOfNullable(request.getHeader("token"), "0000");
                    FilterHelper.addValueToHeaders(request, "uId", uId);
                    FilterHelper.addValueToHeaders(request, "token", token);
                    scope = UserTokenScopeEnum.getUserTypeEnum(propertiesHelper.stringOfNullable(request.getHeader("scope"), "web"));
                    userTokenService.validateUserToken(uId, token, scope);
                    filterChain.doFilter(request, response);
                    break;
                default:
            }
        } catch (RequestRuntimeException e) {
            onError(response, e);
        } catch (RequestException e) {
            onError(response, e);
        } catch (ServiceRuntimeException e) {
            onError(response, e);
        } catch (ServletException e) {
            onError(response, e);
        } catch (IOException e) {
            onError(response, e);
        }
    }

    private void onError(HttpServletResponse response, RequestRuntimeException e) {
        initResponse(response, e.getStateCode(), e.getMessage());
    }

    private void onError(HttpServletResponse response, RequestException e) {
        initResponse(response, e.getStateCode(), e.getMessage());
    }

    private void onError(HttpServletResponse response, ServiceRuntimeException e) {
        initResponse(response, e.getStateCode(), e.getMessage());
    }

    private void onError(HttpServletResponse response, ServletException e) {
        initResponse(response, 500F, e.getMessage());
    }

    private void onError(HttpServletResponse response, IOException e) {
        initResponse(response, 400F, e.getMessage());
    }

    private void initResponse(HttpServletResponse response, Number stateCode, String message) {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        response.setStatus(200);
        PrintWriter out = null;
        try {
            out = response.getWriter();
            out.append("{\n" +
                    "    \"type\": 2,\n" +
                    "    \"code\": " + stateCode.intValue() + ",\n" +
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