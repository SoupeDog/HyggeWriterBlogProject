package org.xavier.blog.user.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.xavier.blog.common.HyggeWriterController;
import org.xavier.blog.user.domain.bo.UserLoginBO;
import org.xavier.blog.user.domain.bo.UserTokenBO;
import org.xavier.blog.user.domain.dto.user.UserTokenDTO;
import org.xavier.blog.user.domain.po.user.UserOperationLog;
import org.xavier.blog.user.service.UserOperationLogServiceImpl;
import org.xavier.blog.user.service.UserTokenServiceImpl;
import org.xavier.common.exception.*;
import org.xavier.web.annotation.EnableControllerLog;
import org.xavier.web.extend.PageResult;

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
@RequestMapping("/user-service/main")
public class UserOperationLogController extends HyggeWriterController {
    @Autowired
    UserOperationLogServiceImpl userOperationLogService;

    @EnableControllerLog
    @PostMapping("/user/log/operation")
    public ResponseEntity<?> addUserOperationLog(@RequestHeader HttpHeaders headers, @RequestBody UserOperationLog userOperationLog) {
        try {
            userOperationLog.validate();
            String operatorUId = headers.getFirst("uId");
            if (!userOperationLogService.saveUserOperationLog(operatorUId, userOperationLog)) {
                throw new Universal_409_X_Exception("Fall to save UserOperationLog:" + jsonHelper.format(userOperationLogService));
            }
            return success();
        } catch (PropertiesException_Runtime e) {
            return fail(HttpStatus.BAD_REQUEST, e.getStateCode(), e.getMessage());
        } catch (Universal_403_X_Exception e) {
            return fail(HttpStatus.FORBIDDEN, e.getStateCode(), e.getMessage());
        } catch (Universal_409_X_Exception e) {
            return fail(HttpStatus.CONFLICT, e.getStateCode(), e.getMessage());
        }
    }

    @EnableControllerLog
    @GetMapping("/user/log/operation/list/{uIdList}")
    public ResponseEntity<?> queryUserOperationLogMultiple(@RequestHeader HttpHeaders headers, @PathVariable(name = "uIdList") ArrayList<String> uIdList,
                                                           @RequestParam(value = "currentPage", required = false, defaultValue = "1") Integer currentPage,
                                                           @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize,
                                                           @RequestParam(value = "orderKey", required = false, defaultValue = "ts") String orderKey,
                                                           @RequestParam(value = "isDESC", required = false, defaultValue = "true") Boolean isDESC) {
        try {
            String operatorUId = headers.getFirst("uId");
            PageResult<UserOperationLog> result = userOperationLogService.quarryUserOperationLogByUIdList(operatorUId, uIdList, currentPage, pageSize, orderKey, isDESC);
            return success(result);
        } catch (PropertiesException_Runtime e) {
            return fail(HttpStatus.BAD_REQUEST, e.getStateCode(), e.getMessage());
        } catch (Universal_403_X_Exception e) {
            return fail(HttpStatus.FORBIDDEN, e.getStateCode(), e.getMessage());
        }
    }
}