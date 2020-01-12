package org.xavier.blog.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.xavier.blog.common.HyggeWriterController;
import org.xavier.blog.domain.po.UserOperationLog;
import org.xavier.blog.service.UserOperationLogServiceImpl;
import org.xavier.common.exception.PropertiesRuntimeException;
import org.xavier.common.exception.Universal403Exception;
import org.xavier.common.exception.Universal404Exception;
import org.xavier.common.exception.Universal409Exception;
import org.xavier.webtoolkit.domain.PageResult;

import java.util.ArrayList;

/**
 * 描述信息：<br/>
 * 用户操作日志模块
 *
 * @author Xavier
 * @version 1.0
 * @date 2018/7/3
 * @since Jdk 1.8
 */
@RestController
@RequestMapping("/blog-service")
public class UserOperationLogController extends HyggeWriterController {
    @Autowired
    UserOperationLogServiceImpl userOperationLogService;

    @PostMapping("/main/user/log/operation")
    public ResponseEntity<?> addUserOperationLog(@RequestHeader HttpHeaders headers, @RequestBody UserOperationLog userOperationLog) {
        try {
            userOperationLog.validate();
            String operatorUId = headers.getFirst("uid");
            if (!userOperationLogService.saveUserOperationLog(operatorUId, userOperationLog)) {
                throw new Universal409Exception("Fall to save UserOperationLog:" + jsonHelper.format(userOperationLogService));
            }
            return success();
        } catch (PropertiesRuntimeException e) {
            return fail(HttpStatus.BAD_REQUEST, e.getStateCode(), e.getMessage());
        } catch (Universal403Exception e) {
            return fail(HttpStatus.FORBIDDEN, e.getStateCode(), e.getMessage());
        } catch (Universal409Exception e) {
            return fail(HttpStatus.CONFLICT, e.getStateCode(), e.getMessage());
        } catch (Universal404Exception e) {
            return fail(HttpStatus.NOT_FOUND, e.getStateCode(), e.getMessage());
        }
    }

    @GetMapping("/main/user/log/operation/list/{uIdList}")
    public ResponseEntity<?> queryUserOperationLogMultiple(@RequestHeader HttpHeaders headers, @PathVariable(name = "uIdList") ArrayList<String> uIdList,
                                                           @RequestParam(value = "currentPage", required = false, defaultValue = "1") Integer currentPage,
                                                           @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize,
                                                           @RequestParam(value = "orderKey", required = false, defaultValue = "ts") String orderKey,
                                                           @RequestParam(value = "isDESC", required = false, defaultValue = "true") Boolean isDESC) {
        try {
            String operatorUId = headers.getFirst("uid");
            PageResult<UserOperationLog> result = userOperationLogService.quarryUserOperationLogByUidList(operatorUId, uIdList, currentPage, pageSize, orderKey, isDESC);
            return success(result);
        } catch (PropertiesRuntimeException e) {
            return fail(HttpStatus.BAD_REQUEST, e.getStateCode(), e.getMessage());
        } catch (Universal403Exception e) {
            return fail(HttpStatus.FORBIDDEN, e.getStateCode(), e.getMessage());
        } catch (Universal404Exception e) {
            return fail(HttpStatus.NOT_FOUND, e.getStateCode(), e.getMessage());
        }
    }
}