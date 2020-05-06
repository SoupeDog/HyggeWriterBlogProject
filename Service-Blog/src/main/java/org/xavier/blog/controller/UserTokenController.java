package org.xavier.blog.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.xavier.blog.common.HyggeWriterController;
import org.xavier.blog.common.enums.UserTypeEnum;
import org.xavier.blog.domain.bo.UserLoginBO;
import org.xavier.blog.domain.bo.UserTokenBO;
import org.xavier.blog.domain.dto.user.UserDTO;
import org.xavier.blog.domain.dto.user.UserTokenDTO;
import org.xavier.blog.domain.po.User;
import org.xavier.blog.domain.po.UserToken;
import org.xavier.blog.service.UserServiceImpl;
import org.xavier.blog.service.UserTokenServiceImpl;
import org.xavier.common.exception.*;

/**
 * 描述信息：<br/>
 * 用户登录模块
 *
 * @author Xavier
 * @version 1.0
 * @date 2018/7/3
 * @since Jdk 1.8
 */
@RestController
@RequestMapping("/blog-service")
public class UserTokenController extends HyggeWriterController {
    @Autowired
    UserTokenServiceImpl userTokenService;
    @Autowired
    UserServiceImpl userService;

    @PostMapping("/extra/token/login")
    public ResponseEntity<?> login(@RequestBody UserLoginBO userLoginBO) {
        try {
            UserTokenDTO result = userTokenService.login(userLoginBO, System.currentTimeMillis());

            return success(result);
        } catch (PropertiesRuntimeException e) {
            return fail(HttpStatus.BAD_REQUEST, e.getStateCode(), e.getMessage());
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

    @PostMapping("/extra/token/validate")
    public ResponseEntity<?> validateToken(@RequestHeader HttpHeaders headers, @RequestBody UserTokenBO userTokenBO) {
        try {
            userTokenBO.validate();
            String loginUid = propertiesHelper.string(headers.getFirst("uid"));
            User loginUser = userService.queryUserNotNull(loginUid);
            userService.checkRight(loginUser, UserTypeEnum.ROOT);
            userTokenService.validateUserToken(userTokenBO.getUid(), userTokenBO.getToken(), userTokenBO.calculateScope(), System.currentTimeMillis());
            return success(true);
        } catch (Universal403Exception e) {
            // token 错误或过期
            return success(false);
        } catch (Universal404Exception e) {
            // 目标用户 token 不存在
            return success(false);
        } catch (PropertiesRuntimeException e) {
            return fail(HttpStatus.BAD_REQUEST, e.getStateCode(), e.getMessage());
        } catch (Universal400Exception e) {
            return fail(HttpStatus.BAD_REQUEST, e.getStateCode(), e.getMessage());
        }
    }

    @PostMapping("/extra/token/keep")
    public ResponseEntity<?> keepToken(@RequestBody UserTokenBO userTokenBO) {
        try {
            userTokenBO.validate();
            UserToken resultTemp = userTokenService.keepAlive(userTokenBO.getUid(), userTokenBO.getToken(), userTokenBO.getRefreshKey(), userTokenBO.calculateScope(), System.currentTimeMillis());
            UserTokenDTO result = new UserTokenDTO(resultTemp);
            User targetUser = userService.queryUserByUId(result.getUid());
            result.setUserInfo(new UserDTO(targetUser));
            return success(result);
        } catch (PropertiesRuntimeException e) {
            return fail(HttpStatus.BAD_REQUEST, e.getStateCode(), e.getMessage());
        } catch (Universal400Exception e) {
            return fail(HttpStatus.BAD_REQUEST, e.getStateCode(), e.getMessage());
        } catch (Universal403Exception e) {
            return fail(HttpStatus.FORBIDDEN, e.getStateCode(), e.getMessage());
        } catch (Universal409Exception e) {
            return fail(HttpStatus.CONFLICT, e.getStateCode(), e.getMessage());
        } catch (Universal404Exception e) {
            return fail(HttpStatus.NOT_FOUND, e.getStateCode(), e.getMessage());
        }
    }
}