package org.xavier.blog.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.xavier.blog.common.filter.CrossOriginFilter;
import org.xavier.blog.filter.LoginFilter;
import org.xavier.blog.service.impl.UserServiceImpl;
import org.xavier.blog.service.impl.UserTokenServiceImpl;

/**
 * 描述信息：<br/>
 *
 * @author Xavier
 * @version 1.0
 * @date 2018/5/30
 * @since Jdk 1.8
 */
@Configuration
public class FilterConfig {
    @Autowired
    UserTokenServiceImpl userTokenService;
    @Autowired
    UserServiceImpl userService;

    //     跨域 plan A
    @Bean
    public FilterRegistrationBean crossOriginFilterRegistrationBean() {
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        filterRegistrationBean.setFilter(new CrossOriginFilter());
        filterRegistrationBean.addUrlPatterns("/*");
        filterRegistrationBean.setOrder(Ordered.LOWEST_PRECEDENCE - 1);
        return filterRegistrationBean;
    }

    @Bean
    public FilterRegistrationBean loginFilterRegistration() {
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        filterRegistrationBean.setFilter(new LoginFilter(userTokenService, userService));
        filterRegistrationBean.addUrlPatterns("/blog-service/main/*");
        filterRegistrationBean.setOrder(Ordered.LOWEST_PRECEDENCE);
        return filterRegistrationBean;
    }
}