package org.xavier.blog.article.service.remote;

import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.stereotype.Service;
import org.xavier.blog.common.ErrorCode;
import org.xavier.blog.common.GatewayResponse;
import org.xavier.blog.common.enums.UserTokenScopeEnum;
import org.xavier.common.exception.Universal403Exception;
import org.xavier.common.exception.Universal500RuntimeException;
import org.xavier.common.util.http.helper.HttpHelperResponse;

/**
 * 描述信息：<br/>
 *
 * @author Xavier
 * @version 1.0
 * @date 2019/4/18
 * @since Jdk 1.8
 */
@Service
public class UserTokenServiceImpl extends DefaultRemoteService {
    private static final TypeReference RESPONSE_TYPE_REFERENCE = new TypeReference<GatewayResponse<Boolean>>() {
    };

    public void validateUserToken(String uId, String token, UserTokenScopeEnum scope) throws Universal403Exception {
        try {
            HttpHelperResponse<GatewayResponse<Boolean>> response = httpHelper.post(getUserServicePrefix() + "/user-service/extra/token/validate", String.format("{\"uId\":\"%s\",\"token\":\"%s\",\"scopeByte\":%s}", uId, token, scope.getScope()), RESPONSE_TYPE_REFERENCE);
            if (response.isFail()) {
                throw new Universal500RuntimeException(ErrorCode.REQUEST_FALL_TO_CALL_UPSTREAM_SERVICES.getErrorCod(), "Fall to call User-Service[validateUserToken].", response.getData().getMsg());
            }
            if (!response.getData().getData()) {
                throw new Universal403Exception(ErrorCode.UNEXPECTED_TOKEN.getErrorCod(), "Unexpected Token(" + token + ") of User(" + uId + ").");
            }
        } catch (Exception e) {
            if (e instanceof Universal403Exception) {
                throw e;
            } else {
                logger.error("Fall to call User-Service[validateUserToken].", e);
                throw new Universal500RuntimeException(ErrorCode.REQUEST_FALL_TO_CALL_UPSTREAM_SERVICES.getErrorCod(), "Fall to call User-Service[validateUserToken].");
            }
        }
    }
}