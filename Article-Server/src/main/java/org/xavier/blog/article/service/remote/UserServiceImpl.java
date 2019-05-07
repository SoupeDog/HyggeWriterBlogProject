package org.xavier.blog.article.service.remote;

import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.xavier.blog.article.domain.dto.UserDTO;
import org.xavier.blog.common.ErrorCode;
import org.xavier.common.exception.Universal_500_X_Exception_Runtime;
import org.xavier.common.logging.HyggeLogger;
import org.xavier.common.utils.HttpHelperResponse;
import org.xavier.common.utils.HttpHelpper;
import org.xavier.web.extend.GatewayResponse;

import java.util.ArrayList;

/**
 * 描述信息：<br/>
 * 远程调用用户服务
 *
 * @author Xavier
 * @version 1.0
 * @date 2019/5/7
 * @since Jdk 1.8
 */
@Service
public class UserServiceImpl extends DefaultRemoteService {
    @Autowired
    HttpHelpper httpHelpper;
    @Autowired
    HyggeLogger logger;


    private static final TypeReference RESPONSE_TYPEREFERENCE_USER = new TypeReference<GatewayResponse<ArrayList<UserDTO>>>() {
    };

    public UserDTO queryUserByUId(String uId) {
        HttpHelperResponse<GatewayResponse<ArrayList<UserDTO>>> response = httpHelpper.get(getUserServicePrefix() + "/main/user/" + uId, httpHeaders, RESPONSE_TYPEREFERENCE_USER);
        if (response.isFail()) {
            throw new Universal_500_X_Exception_Runtime(ErrorCode.REQUEST_FALL_TO_CALL_UPSTREAM_SERVICES.getErrorCod(), "Fall to call User-Service[queryUserByUId].", response.getData().getMsg());
        }
        if (response.getData().getData().size() < 0) {
            return null;
        } else {
            return response.getData().getData().get(0);
        }
    }
}