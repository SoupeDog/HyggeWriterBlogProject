package org.xavier.blog.article.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.xavier.blog.common.HyggeWriterController;
import org.xavier.common.utils.UtilsCreator;

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
public class Test extends HyggeWriterController {
    @GetMapping("/main")
    public Object test() {
        return new HashMap() {{
            put("name", "张三");
        }};
    }

    public static void main(String[] args) {
        String string = "7f0e0ef4ee003180aafeea09462e8dec-程序员/cbf6912b977d34809e1acca515c5e06e-后端/6f907edfaa5e376282a72b6b380c9da4-Java";
        String[] array = string.split("/");


        for(String temp:array){

        }
        System.out.println(UtilsCreator.getInstance_DefaultJsonHelper(true).format(array));

    }
}
