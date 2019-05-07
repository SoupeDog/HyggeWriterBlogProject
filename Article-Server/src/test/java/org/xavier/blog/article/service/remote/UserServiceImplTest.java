package org.xavier.blog.article.service.remote;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.xavier.common.exception.Universal_404_X_Exception;
import org.xavier.common.utils.UtilsCreator;

import static org.junit.Assert.*;

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


    @Test
    public void queryUserByUId() throws Universal_404_X_Exception {
        System.out.println(UtilsCreator.getInstance_DefaultJsonHelper(true).format(userService.queryUserByUId_WithExistValidate("U00000004")));
    }
}