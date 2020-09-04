package org.xavier.blog.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.xavier.blog.common.ErrorCode;
import org.xavier.blog.common.HyggeWriterController;
import org.xavier.blog.domain.po.User;
import org.xavier.blog.service.impl.UserServiceImpl;
import org.xavier.blog.utils.RequestProcessTrace;
import org.xavier.common.exception.*;

import java.util.ArrayList;
import java.util.Map;

/**
 * 描述信息：<br/>
 * 用户操作
 *
 * @author Xavier
 * @version 1.0
 * @date 2018/2/9
 * @since Jdk 1.8
 */
@RestController
@RequestMapping("/blog-service")
public class UserController extends HyggeWriterController {
    @Autowired
    UserServiceImpl userService;

    @PostMapping(value = "/main/user")
    public ResponseEntity<?> saveUser(@RequestBody User user) {
        try {
            userService.saveUser(user, System.currentTimeMillis());
            return success(user);
        } catch (PropertiesRuntimeException e) {
            return fail(HttpStatus.BAD_REQUEST, e.getStateCode(), e.getMessage());
        } catch (DuplicateKeyException e) {
            return fail(HttpStatus.CONFLICT, ErrorCode.USER_EXISTS.getErrorCod(), "User(" + user.getUserName() + ") dose exist.");
        }
    }


    @DeleteMapping(value = "/main/user/{uidList}")
    public ResponseEntity<?> removeUserMultiple(@RequestParam(value = "upTs", required = false) String upTsTemp,
                                                @PathVariable("uidList") ArrayList<String> uidList) {
        try {
            Long upTs = propertiesHelper.longRangeOfNullable(upTsTemp, System.currentTimeMillis(), "[ts] can't be null.");
            String loginUid = RequestProcessTrace.getContext().getCurrentLoginUid();
            if (!userService.removeUserByUidLogicallyMultiple(loginUid, uidList, upTs)) {
                throw new Universal409Exception(ErrorCode.USER_DELETE_CONFLICT.getErrorCod(), "Remove conflict,please try it again if target still exists.");
            }
            return success();
        } catch (PropertiesRuntimeException e) {
            return fail(HttpStatus.BAD_REQUEST, e.getStateCode(), e.getMessage());
        } catch (Universal403Exception e) {
            return fail(HttpStatus.FORBIDDEN, e.getStateCode(), e.getMessage());
        } catch (Universal404Exception e) {
            return fail(HttpStatus.NOT_FOUND, e.getStateCode(), e.getMessage());
        } catch (Universal409Exception e) {
            return fail(HttpStatus.CONFLICT, e.getStateCode(), e.getMessage());
        }
    }

    @PutMapping(value = "/main/user/{uid}")
    public ResponseEntity<?> updateUser(@RequestParam(value = "upTs", required = false) String upTsTemp,
                                        @PathVariable("uid") String uid, @RequestBody Map rawData) {
        try {
            Long upTs = propertiesHelper.longRangeOfNullable(upTsTemp, System.currentTimeMillis(), "[ts] can't be null.");
            String loginUid = RequestProcessTrace.getContext().getCurrentLoginUid();
            if (!userService.updateUser(loginUid, uid, rawData, upTs)) {
                throw new Universal409Exception(ErrorCode.USER_UPDATE_CONFLICT.getErrorCod(), "Update conflict,please try it again if target still exists.");
            }
            return success();
        } catch (PropertiesRuntimeException e) {
            return fail(HttpStatus.BAD_REQUEST, e.getStateCode(), e.getMessage());
        } catch (DuplicateKeyException e) {
            return fail(HttpStatus.CONFLICT, ErrorCode.USER_EXISTS.getErrorCod(), "Name of User should be unique.");
        } catch (Universal400Exception e) {
            return fail(HttpStatus.BAD_REQUEST, e.getStateCode(), e.getMessage());
        } catch (Universal403Exception e) {
            return fail(HttpStatus.FORBIDDEN, e.getStateCode(), e.getMessage());
        } catch (Universal404Exception e) {
            return fail(HttpStatus.NOT_FOUND, e.getStateCode(), e.getMessage());
        } catch (Universal409Exception e) {
            return fail(HttpStatus.CONFLICT, e.getStateCode(), e.getMessage());
        }
    }

    @GetMapping(value = "/main/user/{uidList}")
    public ResponseEntity<?> queryUserMultiple(@PathVariable("uidList") ArrayList<String> uidList) {
        try {
            String loginUid = RequestProcessTrace.getContext().getCurrentLoginUid();
            ArrayList<User> result = userService.queryUserListByUid(uidList, loginUid);
            return success(result);
        } catch (PropertiesRuntimeException e) {
            return fail(HttpStatus.BAD_REQUEST, e.getStateCode(), e.getMessage());
        } catch (Universal404Exception e) {
            return fail(HttpStatus.NOT_FOUND, e.getStateCode(), e.getMessage());
        } catch (Universal403Exception e) {
            return fail(HttpStatus.FORBIDDEN, e.getStateCode(), e.getMessage());
        }
    }
}