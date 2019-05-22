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
    @Autowired
    GroupServiceImpl groupService;

    private static final TypeReference RESPONSE_TYPEREFERENCE_USER_LIST = new TypeReference<GatewayResponse<ArrayList<UserDTO>>>() {
    };

    public UserValidateBO queryUserValidateBOByUId(String uId, String secretKey) {
        UserValidateBO result = new UserValidateBO();
        UserDTO user = queryUserByUId(uId);
        result.setUser(user);
        ArrayList<String> groupIdList = groupService.quarryGroupInfoOfUser(uId);
        result.setGroupIdList(groupIdList);
        result.setSecretKey(secretKey);
        return result;
    }

    public UserDTO queryUserByUId(String uId) {
        HttpHelperResponse<GatewayResponse<ArrayList<UserDTO>>> response = httpHelpper.get(getUserServicePrefix() + "/main/user/" + uId, httpHeaders, RESPONSE_TYPEREFERENCE_USER_LIST);
        if (response.isFail()) {
            throw new Universal_500_X_Exception_Runtime(ErrorCode.REQUEST_FALL_TO_CALL_UPSTREAM_SERVICES.getErrorCod(), "Fall to call User-Service[queryUserByUId].", response.getData().getMsg());
        }
        if (response.getData().getData().size() < 1) {
            return null;
        } else {
            return response.getData().getData().get(0);
        }
    }

    public UserDTO queryUserByUId_WithExistValidate(String uId) throws Universal_404_X_Exception {
        UserDTO result = queryUserByUId(uId);
        if (result == null) {
            throw new Universal_404_X_Exception(ErrorCode.USER_NOTFOUND.getErrorCod(), "User(" + uId + ") was not found.");
        }
        return result;
    }

    public void checkRight(String operatorUId, String expectedUId) throws Universal_403_X_Exception {
        UserDTO currentOperator = queryUserByUId(operatorUId);
        if (currentOperator == null) {
            throw new Universal_403_X_Exception(ErrorCode.INSUFFICIENT_PERMISSIONS.getErrorCod(), "Insufficient Permissions.");
        } else {
            if (!currentOperator.getUserType().equals(UserTypeEnum.ROOT) && !operatorUId.equals(expectedUId)) {
                throw new Universal_403_X_Exception(ErrorCode.INSUFFICIENT_PERMISSIONS.getErrorCod(), "Insufficient Permissions.");
            }
        }
    }
}