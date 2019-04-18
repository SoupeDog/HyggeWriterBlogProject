package org.xavier.blog.article.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.xavier.blog.article.filter.LoginFilter;
import org.xavier.blog.article.service.UserTokenServiceImpl;

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

    @Bean
    public FilterRegistrationBean loginFilterRegistration() {
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        filterRegistrationBean.setFilter(new LoginFilter(userTokenService));
        filterRegistrationBean.setOrder(Ordered.LOWEST_PRECEDENCE);
        filterRegistrationBean.addUrlPatterns("/main/*");
        return filterRegistrationBean;
    }
}