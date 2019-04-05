package org.xavier.blog.article.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

/**
 * 描述信息：<br/>
 *
 * @author Xavier
 * @version 1.0
 * @date 2019/3/24
 * @since Jdk 1.8
 */
@RestController
public class Test {

    @GetMapping("/test")
    public Object test() {
        return new HashMap() {{
            put("name", "张三");
        }};
    }
}
