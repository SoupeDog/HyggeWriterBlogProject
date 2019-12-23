package org.xavier.blog;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.xavier.blog.dao.UserMapper;
import org.xavier.blog.domain.po.User;

/**
 * 描述信息：<br/>
 *
 * @author Xavier
 * @version 1.0
 * @date 19-12-23
 * @since Jdk 1.8
 */
@SpringBootApplication
@EnableDiscoveryClient
@RestController
public class Application {
    @Autowired
    UserMapper userMapper;

    @GetMapping("/")
    public User get() {
        return userMapper.queryUserByUid("U00000001");
    }


    public static void main(String[] args) {
        ApplicationContext applicationContext = SpringApplication.run(Application.class, args);
    }
}