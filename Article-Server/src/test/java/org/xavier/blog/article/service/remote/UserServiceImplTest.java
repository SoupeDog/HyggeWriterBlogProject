package org.xavier.blog.article.service.remote;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.xavier.blog.article.service.ArticleCategoryServiceImpl;
import org.xavier.common.exception.Universal404Exception;
import org.xavier.common.util.UtilsCreator;

import java.io.IOException;


/**
 * 描述信息：<br/>
 *
 * @author Xavier
 * @version 1.0
 * @date 2019/5/7
 * @since Jdk 1.8
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class UserServiceImplTest {
    @Autowired
    UserServiceImpl userService;

    @Autowired
    ArticleCategoryServiceImpl articleCategoryService;


    @Test
    public void queryUserByUId() throws Universal404Exception {
        Long ts = System.currentTimeMillis();
        Object object = userService.queryUserValidateBOByUId("U00000001", null);
        System.out.println((System.currentTimeMillis() - ts) + " 毫秒");
        System.out.println(UtilsCreator.getDefaultJsonHelperInstance(true).format(object));
    }

    @Test
    public void queryUserByUId2() throws Universal404Exception {
        System.out.println(UtilsCreator.getDefaultJsonHelperInstance(true).format(articleCategoryService.queryArticleCategoryByUId("U00000001", "123", "U00000001", "")));
    }

    @Test
    public void addUserLog() throws InterruptedException, IOException {
        userService.addUserLog_Async("U00000001", "搞事情", "没搞成", "127.0.0.1", "爪机", 1L);
       System.in.read();
    }
}