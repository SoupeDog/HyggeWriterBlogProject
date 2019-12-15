package org.xavier.blog.user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.xavier.blog.common.ErrorCode;
import org.xavier.blog.common.enums.UserTokenScopeEnum;
import org.xavier.blog.user.dao.UserTokenMapper;
import org.xavier.blog.user.domain.bo.UserLoginBO;
import org.xavier.blog.user.domain.po.user.User;
import org.xavier.blog.user.domain.po.user.UserToken;
import org.xavier.common.exception.Universal400Exception;
import org.xavier.common.exception.Universal403Exception;
import org.xavier.common.exception.Universal404Exception;
import org.xavier.common.exception.Universal409Exception;
import org.xavier.common.logging.HyggeLoggerMsgBuilder;
import org.xavier.webtoolkit.base.DefaultUtils;

import java.util.LinkedHashMap;


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

    public UserToken login(UserLoginBO loginBO, Long currentTs) throws Universal403Exception, Universal409Exception, Universal400Exception, Universal404Exception {
        User targetUser = userService.queryUserNotNull(loginBO.getUid());
        UserTokenScopeEnum currentUserTokenScope = loginBO.calculateScope();
        if (!targetUser.getPw().equals(loginBO.getPw())) {
            throw new Universal403Exception(ErrorCode.UNEXPECTED_PASSWORD_OR_UID.getErrorCod(), "Unexpected password or unexpected uid.");
        }
        UserToken currentUserToken = userTokenMapper.queryUserByUidAndScope(loginBO.getUid(), currentUserTokenScope.getScope());
        if (currentUserToken == null) {
            currentUserToken = new UserToken();
            currentUserToken.firstInit(targetUser.getUid(), currentUserTokenScope, currentTs);
            try {
                userTokenMapper.saveUserToken(currentUserToken);
            } catch (DuplicateKeyException e) {
                throw new Universal409Exception(ErrorCode.TOKEN_CREATE_CONFLICT.getErrorCod(), "Login conflict,please try it again later.");
            }
        } else {
            currentUserToken = refreshToken(currentTs, targetUser.getUid(), currentUserTokenScope, currentUserToken);
        }
        return currentUserToken;
    }

    public UserToken keepAlive(String uid, String token, String refreshKey, UserTokenScopeEnum scope, Long currentTs) throws Universal404Exception, Universal409Exception, Universal403Exception {
        UserToken userToken = queryUserByUidAndScopeNotNull(uid, scope.getScope());
        if (userToken.getDeadLine() > currentTs) {
            if (!userToken.getToken().equals(token) || !userToken.getScope().equals(scope)) {
                // 新令牌失效
                if (userToken.getLastDeadLine() > currentTs) {
                    if (!userToken.getLastToken().equals(token) || !userToken.getScope().equals(scope)) {
                        throw new Universal403Exception(ErrorCode.UNEXPECTED_TOKEN.getErrorCod(), "Unexpected Token(" + token + ") of User(" + uid + ").");
                    }
                } else {
                    // 旧令牌已过期
                    userToken = refreshTokenForKeepAlive(uid, token, refreshKey, scope, currentTs, userToken);
                }
            }
        } else {
            // 新令牌已过期
            userToken = refreshTokenForKeepAlive(uid, token, refreshKey, scope, currentTs, userToken);
        }
        return userToken;
    }

    public void validateUserToken(String uid, String token, UserTokenScopeEnum scope, Long currentTs) throws Universal404Exception, Universal403Exception {
        UserToken userToken = queryUserByUidAndScopeNotNull(uid, scope.getScope());
        if (currentTs > userToken.getDeadLine()) {
            throw new Universal403Exception(ErrorCode.TOKEN_OVERDUE.getErrorCod(), "Token(" + token + ") of User(" + uid + ") was overdue.");
        } else {
            if (!token.equals(userToken.getToken())) {
                if (currentTs > userToken.getLastDeadLine()) {
                    throw new Universal403Exception(ErrorCode.TOKEN_OVERDUE.getErrorCod(), "Token(" + token + ") of User(" + uid + ") was overdue.");
                }
                if (!token.equals(userToken.getLastToken())) {
                    throw new Universal403Exception(ErrorCode.UNEXPECTED_TOKEN.getErrorCod(), "Unexpected Token(" + token + ") of User(" + uid + ").");
                }
            }
        }
    }

    public UserToken queryUserByUidAndScopeNotNull(String uid, Byte scope) throws Universal404Exception {
        UserToken userToken = userTokenMapper.queryUserByUidAndScope(uid, scope);
        if (userToken == null) {
            throw new Universal404Exception(ErrorCode.TOKEN_NOTFOUND.getErrorCod(), "Token of User(" + uid + ") was not found.");
        }
        return userToken;
    }

    private UserToken refreshTokenForKeepAlive(String uid, String token, String refreshKey, UserTokenScopeEnum scope, Long currentTs, UserToken userToken) throws Universal403Exception {
        if (!userToken.getRefreshKey().equals(refreshKey)) {
            // 刷新 key 有误
            throw new Universal403Exception(ErrorCode.UNEXPECTED_TOKEN.getErrorCod(), "Unexpected refreshKey(" + refreshKey + ") of Token(" + token + ").");
        }
        userToken = refreshToken(currentTs, uid, scope, userToken);
        return userToken;
    }

    private UserToken refreshToken(Long currentTs, String uid, UserTokenScopeEnum currentUserTokenScope, UserToken currentUserToken) {
        currentUserToken.refresh(currentTs);
        Integer updateAffectedRow = userTokenMapper.refreshToken(uid, currentUserTokenScope.getScope(),
                currentUserToken.getToken(), currentUserToken.getDeadLine(),
                currentUserToken.getLastToken(), currentUserToken.getLastDeadLine(), currentUserToken.getRefreshKey(), currentTs);
        Boolean updateFlag = updateAffectedRow == 1;
        if (!updateFlag) {
            logger.warn(HyggeLoggerMsgBuilder.assertFail("refreshToken affected row", "1", updateAffectedRow, new LinkedHashMap<String, Object>() {{
                put("uid", uid);
                put("currentUserToken", currentUserToken);
                put("currentTs", currentTs);
            }}));
        }
        return currentUserToken;
    }
}
