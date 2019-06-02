package org.xavier.blog.user.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.xavier.blog.common.HyggeWriterController;
import org.xavier.blog.common.ErrorCode;
import org.xavier.blog.user.domain.dto.user.UserDTO;
import org.xavier.blog.user.domain.po.user.User;
import org.xavier.blog.user.service.UserServiceImpl;
import org.xavier.common.exception.PropertiesException_Runtime;
import org.xavier.common.exception.Universal_403_X_Exception;
import org.xavier.common.exception.Universal_404_X_Exception;
import org.xavier.common.exception.Universal_409_X_Exception;
import org.xavier.common.utils.UtilsCreator;
import org.xavier.web.annotation.EnableControllerLog;

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
@RequestMapping("/user-service/main")
public class UserController extends HyggeWriterController {
    @Autowired
    UserServiceImpl userService;

    @EnableControllerLog
    @PostMapping(value = "/user")
    public ResponseEntity<?> saveUser(@RequestBody User user) {
        try {
            userService.saveUser(user, System.currentTimeMillis());
            return success(userService.userToUserDTO(user));
        } catch (PropertiesException_Runtime e) {
            return fail(HttpStatus.BAD_REQUEST, e.getStateCode(), e.getMessage());
        } catch (DuplicateKeyException e) {
            return fail(HttpStatus.CONFLICT, ErrorCode.USER_EXISTS.getErrorCod(), "User(" + user.getuName() + ") dose exist.");
        }
    }

    @EnableControllerLog(ignoreProperties = "headers")
    @DeleteMapping(value = "/user/{uIds}")
    public ResponseEntity<?> removeUserMultiple(@RequestHeader HttpHeaders headers, @PathVariable("uIds") ArrayList<String> uIdList) {
        try {
            Long currentTs = UtilsCreator.getInstance_DefaultPropertiesHelper().longRangeNotNull(headers.getFirst("ts"), "[ts] can't be null.");
            if (!userService.removeUserByUId_Logically_Multiple(headers.getFirst("uId"), uIdList, currentTs)) {
                throw new Universal_409_X_Exception(ErrorCode.USER_DELETE_CONFLICT.getErrorCod(), "Remove conflict,please try it again if target still exists.");
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

    @EnableControllerLog(ignoreProperties = {"headers"})
    @PutMapping(value = "/user/{uId}")
    public ResponseEntity<?> updateUser(@RequestHeader HttpHeaders headers, @PathVariable("uId") String uId, @RequestBody Map rowData) {
        try {
            if (!userService.updateUser(headers.getFirst("uId"), uId, rowData)) {
                throw new Universal_409_X_Exception(ErrorCode.USER_UPDATE_CONFLICT.getErrorCod(), "Update conflict,please try it again if target still exists.");
            }
            return success();
        } catch (PropertiesException_Runtime e) {
            return fail(HttpStatus.BAD_REQUEST, e.getStateCode(), e.getMessage());
        } catch (DuplicateKeyException e) {
            return fail(HttpStatus.CONFLICT, ErrorCode.USER_EXISTS.getErrorCod(), "Name of User should be unique.");
        } catch (Universal_404_X_Exception e) {
            return fail(HttpStatus.NOT_FOUND, e.getStateCode(), e.getMessage());
        } catch (Universal_403_X_Exception e) {
            return fail(HttpStatus.FORBIDDEN, e.getStateCode(), e.getMessage());
        } catch (Universal_409_X_Exception e) {
            return fail(HttpStatus.CONFLICT, e.getStateCode(), e.getMessage());
        }
    }

    @EnableControllerLog
    @GetMapping(value = "/user/{uIds}")
    public ResponseEntity<?> queryUserMultiple(@PathVariable("uIds") ArrayList<String> uIdList) {
        try {
            ArrayList<UserDTO> result = userService.userToUserDTO(userService.queryUserListByUId(uIdList));
            return success(result);
        } catch (PropertiesException_Runtime e) {
            return fail(HttpStatus.BAD_REQUEST, e.getStateCode(), e.getMessage());
        }
    }
}