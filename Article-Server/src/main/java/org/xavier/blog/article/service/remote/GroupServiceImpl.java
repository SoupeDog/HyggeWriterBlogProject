package org.xavier.blog.article.service.remote;

import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.xavier.blog.article.domain.bo.UserValidateBO;
import org.xavier.blog.article.domain.dto.UserDTO;
import org.xavier.blog.article.domain.enums.UserTypeEnum;
import org.xavier.blog.common.ErrorCode;
import org.xavier.common.exception.Universal_403_X_Exception;
import org.xavier.common.exception.Universal_404_X_Exception;
import org.xavier.common.exception.Universal_500_X_Exception_Runtime;
import org.xavier.common.logging.HyggeLogger;
import org.xavier.common.utils.HttpHelperResponse;
import org.xavier.common.utils.HttpHelpper;
import org.xavier.web.extend.GatewayResponse;

import java.util.ArrayList;

/**
 * 描述信息：<br/>
 * 远程调用用户群组服务
 *
 * @author Xavier
 * @version 1.0
 * @date 2019/5/7
 * @since Jdk 1.8
 */
@Service
public class GroupServiceImpl extends DefaultRemoteService {
    @Autowired
    HttpHelpper httpHelpper;
    @Autowired
    HyggeLogger logger;

    private static final TypeReference RESPONSE_TYPEREFERENCE_STRING_LIST = new TypeReference<GatewayResponse<ArrayList<String>>>() {
    };

    public ArrayList<String> quarryGroupInfoOfUser(String uId) {
        HttpHelperResponse<GatewayResponse<ArrayList<String>>> response = httpHelpper.get(getUserServicePrefix() + "/main/group/list?uId=" + uId, httpHeaders, RESPONSE_TYPEREFERENCE_STRING_LIST);
        if (response.isFail()) {
            throw new Universal_500_X_Exception_Runtime(ErrorCode.REQUEST_FALL_TO_CALL_UPSTREAM_SERVICES.getErrorCod(), "Fall to call User-Service[quarryGroupInfoOfUser].", response.getData().getMsg());
        }
        if (response.getData().getData().size() < 1) {
            return null;
        } else {
            return response.getData().getData();
        }
    }
}