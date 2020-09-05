package org.xavier.blog.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.xavier.blog.common.HyggeWriterController;
import org.xavier.blog.common.enums.UserTypeEnum;
import org.xavier.blog.domain.po.User;
import org.xavier.blog.service.impl.UserServiceImpl;
import org.xavier.blog.task.CopyArticleToESTask;
import org.xavier.blog.utils.RequestProcessTrace;
import org.xavier.common.exception.PropertiesRuntimeException;
import org.xavier.common.exception.Universal403Exception;

/**
 * 描述信息：<br/>
 * 管理员后台
 *
 * @author Xavier
 * @version 1.0
 * @date 2020/9/5
 * @since Jdk 1.8
 */
@RestController
@RequestMapping("/blog-service")
public class AdminController extends HyggeWriterController {
    @Autowired
    CopyArticleToESTask copyArticleToESTask;
    @Autowired
    UserServiceImpl userService;

    @GetMapping(value = "/main/admin/es/syncdb")
    public ResponseEntity<?> saveArticle(@RequestParam(value = "batchSize", required = false, defaultValue = "10") Integer batchSize) {
        User loginUser = RequestProcessTrace.getContext().getCurrentLoginUser();
        try {
            userService.checkRight(loginUser, UserTypeEnum.ROOT);
            return success(copyArticleToESTask.start(batchSize));
        } catch (PropertiesRuntimeException e) {
            return fail(HttpStatus.BAD_REQUEST, e.getStateCode(), e.getMessage());
        } catch (Universal403Exception e) {
            return fail(HttpStatus.FORBIDDEN, e.getStateCode(), e.getMessage());
        }
    }

}