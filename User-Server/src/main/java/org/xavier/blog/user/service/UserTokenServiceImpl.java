package org.xavier.blog.user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.xavier.blog.user.dao.UserTokenMapper;
import org.xavier.blog.common.ErrorCode;
import org.xavier.blog.user.domain.bo.UserLoginBO;
import org.xavier.blog.user.domain.enums.UserTokenScopeEnum;
import org.xavier.blog.user.domain.po.user.User;
import org.xavier.blog.user.domain.po.user.UserToken;
import org.xavier.common.exception.Universal_400_X_Exception;
import org.xavier.common.exception.Universal_403_X_Exception;
import org.xavier.common.exception.Universal_404_X_Exception;
import org.xavier.common.exception.Universal_409_X_Exception;
import org.xavier.common.logging.HyggeLoggerMsgBuilder;
import org.xavier.web.extend.DefaultService;


/**
 * 描述信息：<br/>
 * 用户 token
 *
 * @author Xavier
 * @version 1.0
 * @date 2018/6/27
 * @since Jdk 1.8
 */
@Service
public class UserTokenServiceImpl extends DefaultService {
    @Autowired
    UserTokenMapper userTokenMapper;
    @Autowired
    UserServiceImpl userService;

    public UserToken login(UserLoginBO loginBO) throws Universal_403_X_Exception, Universal_409_X_Exception, Universal_404_X_Exception, Universal_400_X_Exception {
        User targetUser = userService.queryUserByUId_WithExistValidate(loginBO.getuId());
        if (targetUser == null || targetUser.getPw().equals(loginBO.getPw()) != true) {
            throw new Universal_403_X_Exception(ErrorCode.UNEXPECTED_PASSWORD_OR_UID.getErrorCod(), "Unexpected password or unexpected uId.");
        }
        UserToken userToken = new UserToken();
        userToken.firstInit(loginBO.getuId(), loginBO.calculateScope(), System.currentTimeMillis());
        userTokenMapper.removeUserToken(loginBO.getuId(), loginBO.getScopeByte());
        try {
            userTokenMapper.saveUserToken_Single(userToken);
        } catch (DuplicateKeyException e) {
            throw new Universal_409_X_Exception(ErrorCode.TOKEN_CREATE_CONFLICT.getErrorCod(), "Login conflict,please try it again later.");
        }
        return userToken;
    }

    public void validateUserToken(String uId, String token, UserTokenScopeEnum scope) throws Universal_404_X_Exception, Universal_403_X_Exception {
        UserToken userToken = userTokenMapper.queryUserByUIdAndScope(uId, scope.getScope());
        if (userToken == null) {
            throw new Universal_404_X_Exception(ErrorCode.TOKEN_NOTFOUND.getErrorCod(), "Token of User(" + uId + ") was not found.");
        } else {
            Long currentTs = System.currentTimeMillis();
            if (currentTs > userToken.getDeadLine()) {
                throw new Universal_403_X_Exception(ErrorCode.TOKEN_OVERDUE.getErrorCod(), "Token(" + token + ") of User(" + uId + ") was overdue.");
            } else {
                if (!token.equals(userToken.getToken())) {
                    if (!token.equals(userToken.getLastToken())) {
                        throw new Universal_403_X_Exception(ErrorCode.UNEXPECTED_TOKEN.getErrorCod(), "Unexpected Token(" + token + ") of User(" + uId + ").");
                    }
                }
            }
        }
    }

    public UserToken refreshToken(String uId, UserTokenScopeEnum scope, String refreshKey, Long upTs) throws Universal_403_X_Exception, Universal_404_X_Exception, Universal_409_X_Exception {
        UserToken userToken = userTokenMapper.queryUserByUIdAndScope(uId, scope.getScope());
        if (userToken == null) {
            throw new Universal_404_X_Exception(ErrorCode.TOKEN_NOTFOUND.getErrorCod(), "Token of User(" + uId + ") was not found.");
        } else {
            if (userToken.getScope().getScope() != scope.getScope()) {
                throw new Universal_403_X_Exception(ErrorCode.UNEXPECTED_TOKEN_SCOPE.getErrorCod(), "Unexpected scope of token.");
            }
            if (!userToken.getRefreshKey().equals(refreshKey)) {
                throw new Universal_403_X_Exception(ErrorCode.UNEXPECTED_TOKEN_REFRESH_KEY.getErrorCod(), "Unexpected refreshKey of User(" + uId + ").");
            }
            userToken.refresh(System.currentTimeMillis());
            Integer affectedLine = userTokenMapper.refreshToken_CAS(uId, scope.getScope(), userToken.getToken(), userToken.getDeadLine(), userToken.getLastToken(), userToken.getLastDeadLine(), userToken.getRefreshKey(), upTs);
            if (affectedLine != 1) {
                logger.warn(HyggeLoggerMsgBuilder.assertFail("RefreshToken-AffectedLine of User(" + uId + ")", "1", affectedLine, null));
                throw new Universal_409_X_Exception(ErrorCode.TOKEN_REFRESH_CONFLICT.getErrorCod(), "Refresh token conflicting,please try it again later.");
            }
        }
        return userToken;
    }
}
