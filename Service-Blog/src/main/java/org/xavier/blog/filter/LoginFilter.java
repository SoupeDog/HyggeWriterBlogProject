package org.xavier.blog.filter;

import org.springframework.web.filter.OncePerRequestFilter;
import org.xavier.blog.common.filter.FilterHelper;
import org.xavier.blog.common.enums.UserTokenScopeEnum;
import org.xavier.blog.domain.po.User;
import org.xavier.blog.service.UserServiceImpl;
import org.xavier.blog.service.UserTokenServiceImpl;
import org.xavier.blog.utils.HyggeRequestContext;
import org.xavier.blog.utils.RequestProcessTrace;
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
    private UserServiceImpl userService;

    public LoginFilter() {
    }

    public LoginFilter(UserTokenServiceImpl userTokenService, UserServiceImpl userService) {
        this.userTokenService = userTokenService;
        this.userService = userService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) {
        PropertiesHelper propertiesHelper = UtilsCreator.getDefaultPropertiesHelperInstance();
        String uid, token, secretKey;
        UserTokenScopeEnum scope;
        try {
            switch (request.getMethod().toUpperCase()) {
                case "GET":
                case "POST":
                case "PUT":
                case "DELETE":
                    uid = propertiesHelper.stringOfNullable(request.getHeader("uid"), "U00000000");
                    token = propertiesHelper.stringOfNullable(request.getHeader("token"), "0000");
                    secretKey = propertiesHelper.string(request.getHeader("secretKey"));
                    scope = UserTokenScopeEnum.getUserTypeEnum(propertiesHelper.stringOfNullable(request.getHeader("scope"), "web"));
                    userTokenService.validateUserToken(uid, token, scope, System.currentTimeMillis());
                    // 进程上下文中注入登录信息
                    User currentLoginUser = userService.queryUserNotNull(uid);
                    HyggeRequestContext hyggeRequestContext = RequestProcessTrace.getContext();
                    hyggeRequestContext.setCurrentLoginUid(uid);
                    hyggeRequestContext.setCurrentLoginToken(token);
                    hyggeRequestContext.setScope(scope);
                    hyggeRequestContext.setSecretKey(secretKey);
                    hyggeRequestContext.setCurrentLoginUser(currentLoginUser);
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
        } finally {
            // 清理进程上下文
            RequestProcessTrace.clean();
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