package org.xavier.blog.user.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.xavier.blog.user.domain.bo.UserLoginBO;
import org.xavier.blog.user.domain.dto.user.UserTokenDTO;
import org.xavier.blog.user.extend.HyggeWriterController;
import org.xavier.blog.user.service.UserTokenServiceImpl;
import org.xavier.common.exception.*;
import org.xavier.web.annotation.EnableControllerLog;

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
public class UserTokenController extends HyggeWriterController {
    @Autowired
    UserTokenServiceImpl userTokenService;
    @Autowired
    LoadBalancerClient loadBalancerClient;

    @EnableControllerLog
    @PostMapping("/extra/token/login")
    public ResponseEntity<?> createToken(@RequestBody UserLoginBO userLoginBO) {
        try {
            UserTokenDTO result = new UserTokenDTO(userTokenService.login(userLoginBO));
            return success(result);
        } catch (PropertiesException_Runtime e) {
            return fail(HttpStatus.BAD_REQUEST, e.getStateCode(), e.getMessage());
        } catch (Universal_400_X_Exception e) {
            return fail(HttpStatus.BAD_REQUEST, e.getStateCode(), e.getMessage());
        } catch (Universal_403_X_Exception e) {
            return fail(HttpStatus.FORBIDDEN, e.getStateCode(), e.getMessage());
        } catch (Universal_404_X_Exception e) {
            return fail(HttpStatus.NOT_FOUND, e.getStateCode(), e.getMessage());
        } catch (Universal_409_X_Exception e) {
            return fail(HttpStatus.CONFLICT, e.getStateCode(), e.getMessage());
        }
    }

    @EnableControllerLog
    @PostMapping("/extra/token/refresh")
    public ResponseEntity<?> refreshToken(@RequestBody UserLoginBO userLoginBO) {
        try {
            UserTokenDTO result = new UserTokenDTO(userTokenService.refreshToken(userLoginBO.getuId(), userLoginBO.calculateScope(), userLoginBO.getRefreshKey(), System.currentTimeMillis()));
            return success(result);
        } catch (PropertiesException_Runtime e) {
            return fail(HttpStatus.BAD_REQUEST, e.getStateCode(), e.getMessage());
        } catch (Universal_400_X_Exception e) {
            return fail(HttpStatus.BAD_REQUEST, e.getStateCode(), e.getMessage());
        } catch (Universal_403_X_Exception e) {
            return fail(HttpStatus.FORBIDDEN, e.getStateCode(), e.getMessage());
        } catch (Universal_409_X_Exception e) {
            return fail(HttpStatus.CONFLICT, e.getStateCode(), e.getMessage());
        } catch (Universal_404_X_Exception e) {
            return fail(HttpStatus.NOT_FOUND, e.getStateCode(), e.getMessage());
        }
    }
}
