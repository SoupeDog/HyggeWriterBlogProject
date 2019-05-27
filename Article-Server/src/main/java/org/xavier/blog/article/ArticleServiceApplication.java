package org.xavier.blog.article;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.ApplicationContext;

/**
 * 描述信息：<br/>
 *
 * @author Xavier
 * @version 1.0
 * @date 2019/3/18
 * @since Jdk 1.8
 */
@SpringBootApplication
@EnableDiscoveryClient
public class ArticleServiceApplication {
    public static void main(String[] args) {
        ApplicationContext applicationContext = SpringApplication.run(ArticleServiceApplication.class, args);
    }
}
