package org.xavier.blog.common.filter;

import org.springframework.web.filter.OncePerRequestFilter;
import org.xavier.blog.common.PropertiesReminder;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 描述信息：<br/>
 * 粗略允许跨域访问
 *
 * @author Xavier
 * @version 1.0
 * @date 2018/5/30
 * @since Jdk 1.8
 */
public class CrossOriginFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        response.setHeader("Access-Control-Allow-Origin", request.getHeader("Origin"));
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Allow-Methods", "OPTIONS,GET,POST, PUT, DELETE");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type, Access-Control-Allow-Headers, Authorization, X-Requested-With,uid,token,scope,secretKey,ts");
        response.setHeader("Access-Control-Max-Age", "2592000");
        // x-forwarded-for 为 HTTP 头字段标准化草案中正式提出。详见 https://baike.baidu.com/item/X-Forwarded-For
        //        String remoteIp = request.getRemoteAddr();
        String x_Forwarded_For = request.getHeader(PropertiesReminder.DESC_REAL_IP_NAME);
        FilterHelper.addValueToHeaders(request, PropertiesReminder.DESC_REAL_IP_NAME, x_Forwarded_For);
        filterChain.doFilter(request, response);
    }
}