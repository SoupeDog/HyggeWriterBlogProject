package org.xavier.blog.user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
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
public class UserServiceApplication {
    public static void main(String[] args) {
        ApplicationContext applicationContext = SpringApplication.run(UserServiceApplication.class, args);
    }
}
