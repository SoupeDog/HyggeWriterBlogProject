package org.xavier.blog.article.service;

import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.xavier.blog.article.domain.UserTokenScopeEnum;
import org.xavier.blog.common.ErrorCode;
import org.xavier.common.exception.Universal_403_X_Exception;
import org.xavier.common.exception.Universal_500_X_Exception_Runtime;
import org.xavier.common.utils.HttpHelperResponse;
import org.xavier.common.utils.HttpHelpper;
import org.xavier.web.extend.GatewayResponse;

/**
 * 描述信息：<br/>
 *
 * @author Xavier
 * @version 1.0
 * @date 2019/4/18
 * @since Jdk 1.8
 */
@Service
public class UserTokenServiceImpl {
    @Autowired
    HttpHelpper httpHelpper;

    private static final TypeReference RESPONSE_TYPEREFERENCE = new TypeReference<GatewayResponse<Boolean>>() {
    };

    public void validateUserToken(String uId, String token, UserTokenScopeEnum scope) throws Universal_403_X_Exception {
        HttpHelperResponse<GatewayResponse<Boolean>> response = httpHelpper.post("http://127.0.0.1:8080/extra/token/validate", String.format("{\"uId\":\"%s\",\"token\":\"%s\",\"scopeByte\":%s}", uId, token, scope.getScope()), RESPONSE_TYPEREFERENCE);
        if (response.isFail()) {
            throw new Universal_500_X_Exception_Runtime(ErrorCode.REQUEST_FALL_TO_CALL_UPSTREAM_SERVICES.getErrorCod(), "Fall to call User-Service[validateUserToken].");
        }
        if (!response.getData().getData()) {
            throw new Universal_403_X_Exception(ErrorCode.UNEXPECTED_TOKEN.getErrorCod(), "Unexpected Token(" + token + ") of User(" + uId + ").");
        }
    }
}