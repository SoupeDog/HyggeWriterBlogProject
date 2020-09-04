package org.xavier.blog;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

/**
 * 描述信息：<br/>
 *
 * @author Xavier
 * @version 1.0
 * @date 19-12-23
 * @since Jdk 1.8
 */
@SpringBootApplication
//@EnableDiscoveryClient
public class Application {

    public static void main(String[] args) {
        ApplicationContext applicationContext = SpringApplication.run(Application.class, args);
    }
}