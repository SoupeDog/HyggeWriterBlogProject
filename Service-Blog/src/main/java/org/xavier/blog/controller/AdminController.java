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
import org.xavier.common.util.UtilsCreator;

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

    @GetMapping(value = "/main/admin/log/mock")
    public ResponseEntity<?> mockLog(@RequestParam(value = "size", required = false, defaultValue = "10") Integer size) {
        User loginUser = RequestProcessTrace.getContext().getCurrentLoginUser();
        Long uniqueId = UtilsCreator.getDefaultRandomHelperInstance().getSnowFlakeGenerator().createKey();
        String uniqueIdStringVal = uniqueId.toString();
        try {
            userService.checkRight(loginUser, UserTypeEnum.ROOT);
            for (int i = 0; i < size; i++) {
                int value = UtilsCreator.getDefaultRandomHelperInstance().getRandomInteger(0, 100);
                if (value >= 90) {
                    logger.error(String.format("%s-模拟日志数据-%d-错误", uniqueIdStringVal, i), new NullPointerException("模拟空指针"));
                } else if (value >= 80) {
                    logger.error(String.format("%s-模拟日志数据-%d-警告", uniqueIdStringVal, i), new PropertiesRuntimeException("模拟警告"));
                } else {
                    logger.always(String.format("%s-模拟日志数据-%d-正常", uniqueIdStringVal, i));
                }
            }
            return success();
        } catch (PropertiesRuntimeException e) {
            return fail(HttpStatus.BAD_REQUEST, e.getStateCode(), e.getMessage());
        } catch (Universal403Exception e) {
            return fail(HttpStatus.FORBIDDEN, e.getStateCode(), e.getMessage());
        }
    }
}