package org.xavier.blog.article.filter;

import org.springframework.web.filter.OncePerRequestFilter;
import org.xavier.blog.article.service.UserTokenServiceImpl;
import org.xavier.blog.article.domain.enums.UserTokenScopeEnum;
import org.xavier.common.exception.base.RequestException;
import org.xavier.common.exception.base.RequestException_Runtime;
import org.xavier.common.utils.PropertiesHelper;
import org.xavier.common.utils.UtilsCreator;

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
    private UserTokenServiceImpl tokenService;

    public LoginFilter() {
    }

    public LoginFilter(UserTokenServiceImpl tokenService) {
        this.tokenService = tokenService;
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
                case "DELETE":
                case "PUT":
                    uId = propertiesHelper.stringNotNull(request.getHeader("uId"), "[uId] can't be null.");
                    token = propertiesHelper.stringNotNull(request.getHeader("token"), "[token] can't be null.");
                    scope = UserTokenScopeEnum.getUserTypeEnum(propertiesHelper.stringNotNull(request.getHeader("scope"), "[scope] can't be null."));
                    tokenService.validateUserToken(uId, token, scope);
                    filterChain.doFilter(request, response);
                    break;
                default:
            }
        } catch (RequestException_Runtime e) {
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
            out.append(String.format("{\n    \"type\": 2,\n    \"code\": %s,\n    \"msg\": \"%s\",\n    \"data\": null,\n    \"ts\": %d\n}", stateCode, message, System.currentTimeMillis()));
        } catch (IOException e2) {
            e2.printStackTrace();
        } finally {
            if (out != null) {
                out.close();
            }
        }
    }
}
