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
import org.xavier.common.exception.Universal400Exception;
import org.xavier.common.exception.Universal403Exception;
import org.xavier.common.exception.Universal404Exception;
import org.xavier.common.exception.Universal409Exception;
import org.xavier.common.logging.HyggeLoggerMsgBuilder;
import org.xavier.webtoolkit.base.DefaultUtils;


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
public class UserTokenServiceImpl extends DefaultUtils {
    @Autowired
    UserTokenMapper userTokenMapper;
    @Autowired
    UserServiceImpl userService;

    public UserToken login(UserLoginBO loginBO) throws Universal403Exception, Universal409Exception, Universal400Exception, Universal404Exception {
        User targetUser = userService.queryUserByUIdWithExistValidate(loginBO.getuId());
        if (targetUser.getPw().equals(loginBO.getPw()) != true) {
            throw new Universal403Exception(ErrorCode.UNEXPECTED_PASSWORD_OR_UID.getErrorCod(), "Unexpected password or unexpected uId.");
        }
        UserToken userToken = new UserToken();
        userToken.firstInit(loginBO.getuId(), loginBO.calculateScope(), System.currentTimeMillis());
        userTokenMapper.removeUserToken(loginBO.getuId(), userToken.getScope().getScope());
        try {
            userTokenMapper.saveUserToken(userToken);
        } catch (DuplicateKeyException e) {
            throw new Universal409Exception(ErrorCode.TOKEN_CREATE_CONFLICT.getErrorCod(), "Login conflict,please try it again later.");
        }
        return userToken;
    }

    public UserToken keepAlive(String uId, String token, String refreshKey, UserTokenScopeEnum scope, Long currentTs) throws Universal404Exception, Universal409Exception, Universal403Exception {
        UserToken userToken = userTokenMapper.queryUserByUIdAndScope(uId, scope.getScope());
        if (userToken == null) {
            throw new Universal404Exception(ErrorCode.TOKEN_NOTFOUND.getErrorCod(), "Token of User(" + uId + ") was not found.");
        } else {
            if (!userToken.getToken().equals(token) || !userToken.getScope().equals(scope)) {
                if (userToken.getRefreshKey().equals(refreshKey)) {
                    userToken = refreshToken(uId, scope, refreshKey, currentTs);
                } else {
                    throw new Universal403Exception(ErrorCode.UNEXPECTED_TOKEN.getErrorCod(), "Unexpected Token(" + token + ") of User(" + uId + ").");
                }
            }
        }
        return userToken;
    }

    public void validateUserToken(String uId, String token, UserTokenScopeEnum scope) throws Universal404Exception, Universal403Exception {
        UserToken userToken = userTokenMapper.queryUserByUIdAndScope(uId, scope.getScope());
        if (userToken == null) {
            throw new Universal404Exception(ErrorCode.TOKEN_NOTFOUND.getErrorCod(), "Token of User(" + uId + ") was not found.");
        } else {
            Long currentTs = System.currentTimeMillis();
            if (currentTs > userToken.getDeadLine()) {
                throw new Universal403Exception(ErrorCode.TOKEN_OVERDUE.getErrorCod(), "Token(" + token + ") of User(" + uId + ") was overdue.");
            } else {
                if (!token.equals(userToken.getToken())) {
                    if (currentTs > userToken.getLastDeadLine()) {
                        throw new Universal403Exception(ErrorCode.TOKEN_OVERDUE.getErrorCod(), "Token(" + token + ") of User(" + uId + ") was overdue.");
                    }
                    if (!token.equals(userToken.getLastToken())) {
                        throw new Universal403Exception(ErrorCode.UNEXPECTED_TOKEN.getErrorCod(), "Unexpected Token(" + token + ") of User(" + uId + ").");
                    }
                }
            }
        }
    }

    public UserToken refreshToken(String uId, UserTokenScopeEnum scope, String refreshKey, Long upTs) throws Universal403Exception, Universal404Exception, Universal409Exception {
        UserToken userToken = userTokenMapper.queryUserByUIdAndScope(uId, scope.getScope());
        if (userToken == null) {
            throw new Universal404Exception(ErrorCode.TOKEN_NOTFOUND.getErrorCod(), "Token of User(" + uId + ") was not found.");
        } else {
            if (!userToken.getScope().getScope().equals(scope.getScope())) {
                throw new Universal403Exception(ErrorCode.UNEXPECTED_TOKEN_SCOPE.getErrorCod(), "Unexpected scope of token.");
            }
            if (!userToken.getRefreshKey().equals(refreshKey)) {
                throw new Universal403Exception(ErrorCode.UNEXPECTED_TOKEN_REFRESH_KEY.getErrorCod(), "Unexpected refreshKey of User(" + uId + ").");
            }
            userToken.refresh(System.currentTimeMillis());
            Integer affectedRow = userTokenMapper.refreshToken(uId, scope.getScope(), userToken.getToken(), userToken.getDeadLine(), userToken.getLastToken(), userToken.getLastDeadLine(), userToken.getRefreshKey(), upTs);
            if (affectedRow != 1) {
                logger.warn(HyggeLoggerMsgBuilder.assertFail("RefreshToken-affectedRow of User(" + uId + ")", "1", affectedRow, null));
                throw new Universal409Exception(ErrorCode.TOKEN_REFRESH_CONFLICT.getErrorCod(), "Refresh token conflicting,please try it again later.");
            }
        }
        return userToken;
    }
}
