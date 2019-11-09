package org.xavier.blog.user.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.xavier.blog.common.HyggeWriterController;
import org.xavier.blog.user.domain.bo.UserLoginBO;
import org.xavier.blog.user.domain.bo.UserTokenBO;
import org.xavier.blog.user.domain.dto.user.UserTokenDTO;
import org.xavier.blog.user.domain.po.user.UserToken;
import org.xavier.blog.user.service.UserTokenServiceImpl;
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
@RequestMapping("/user-service/extra")
public class UserTokenController extends HyggeWriterController {
    @Autowired
    UserTokenServiceImpl userTokenService;

    @PostMapping("/token/login")
    public ResponseEntity<?> createToken(@RequestBody UserLoginBO userLoginBO) {
        try {
            UserTokenDTO result = new UserTokenDTO(userTokenService.login(userLoginBO));
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

    @PostMapping("/token/validate")
    public ResponseEntity<?> validateToken(@RequestBody UserTokenBO userTokenBO) {
        try {
            userTokenBO.validate();
            userTokenService.validateUserToken(userTokenBO.getuId(), userTokenBO.getToken(), userTokenBO.calculateScope());
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

    @PostMapping("/token/refresh")
    public ResponseEntity<?> refreshToken(@RequestBody UserLoginBO userLoginBO) {
        try {
            UserTokenDTO result = new UserTokenDTO(userTokenService.refreshToken(userLoginBO.getuId(), userLoginBO.calculateScope(), userLoginBO.getRefreshKey(), System.currentTimeMillis()));
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

    @PostMapping("/token/keep")
    public ResponseEntity<?> keepToken(@RequestBody UserTokenBO userTokenBO) {
        try {
            userTokenBO.validate();
            UserToken resultTemp = userTokenService.keepAlive(userTokenBO.getuId(), userTokenBO.getToken(), userTokenBO.getRefreshKey(), userTokenBO.calculateScope(), System.currentTimeMillis());
            if (resultTemp == null) {
                return success();
            } else {
                UserTokenDTO result = new UserTokenDTO(resultTemp);
                return success(result);
            }
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