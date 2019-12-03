package org.xavier.blog.article.service.remote;

import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.xavier.blog.article.domain.bo.UserValidateBO;
import org.xavier.blog.article.domain.dto.UserDTO;
import org.xavier.blog.common.ErrorCode;
import org.xavier.blog.common.GatewayResponse;
import org.xavier.blog.common.enums.UserTypeEnum;
import org.xavier.common.exception.Universal403Exception;
import org.xavier.common.exception.Universal404Exception;
import org.xavier.common.exception.Universal500RuntimeException;
import org.xavier.common.logging.core.HyggeLogger;
import org.xavier.common.util.http.helper.HttpHelper;
import org.xavier.common.util.http.helper.HttpHelperResponse;

import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;

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
    HttpHelper httpHelper;
    @Autowired
    HyggeLogger logger;
    @Autowired
    GroupServiceImpl groupService;

    private static final TypeReference RESPONSE_TYPEREFERENCE_USER_LIST = new TypeReference<GatewayResponse<ArrayList<UserDTO>>>() {
    };
    private static final TypeReference RESPONSE_TYPEREFERENCE_VOID = new TypeReference<GatewayResponse<String>>() {
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
        Long ts = System.currentTimeMillis();
        HttpHelperResponse<GatewayResponse<ArrayList<UserDTO>>> response = httpHelper.get(getUserServicePrefix() + "/user-service/main/user/" + uId, httpHeaders, RESPONSE_TYPEREFERENCE_USER_LIST);
        System.out.println((System.currentTimeMillis() - ts) + " 毫秒=查询用户信息");
        if (response.isFail()) {
            throw new Universal500RuntimeException(ErrorCode.REQUEST_FALL_TO_CALL_UPSTREAM_SERVICES.getErrorCod(), "Fall to call User-Service[queryUserByUId].", response.getData().getMsg());
        }
        if (response.getData().getData().size() < 1) {
            return null;
        } else {
            return response.getData().getData().get(0);
        }
    }

    public void addUserLog_Async(String uId, String behavior, String mainPoints, String realIp, String userAgent, Long ts) {
        String requestJson = String.format("{\"uId\":\"%s\",\"behavior\":\"%s\",\"mainPoints\":\"%s\",\"realIp\":\"%s\",\"userAgent\":\"%s\",\"ts\":%s}",
                uId,
                behavior,
                mainPoints,
                realIp,
                userAgent,
                ts.toString());
        CompletableFuture.runAsync(() -> {
            System.out.println(getUserServicePrefix() + "/main/user/log/operation");
            HttpHelperResponse<GatewayResponse<String>> response = httpHelper.post(getUserServicePrefix() + "/user-service/main/user/log/operation", requestJson, httpHeaders, RESPONSE_TYPEREFERENCE_VOID);
            if (response.isFail()) {
                throw new Universal500RuntimeException(ErrorCode.REQUEST_FALL_TO_CALL_UPSTREAM_SERVICES.getErrorCod(), "Fall to call User-Service[saveUserOperationLog]. " + response.getHttpStatus() + " " + requestJson);
            }
        }).exceptionally(throwable -> {
            if (throwable != null) {
                logger.error(throwable.getMessage(), throwable);
            }
            return null;
        });
    }

    public UserDTO queryUserByUId_WithExistValidate(String uId) throws Universal404Exception {
        UserDTO result = queryUserByUId(uId);
        if (result == null) {
            throw new Universal404Exception(ErrorCode.USER_NOTFOUND.getErrorCod(), "User(" + uId + ") was not found.");
        }
        return result;
    }

    public void checkRight(String currentUserUId, UserTypeEnum expectedUserType, String... uIdWhiteList) throws Universal403Exception {
        UserDTO currentOperator = queryUserByUId(currentUserUId);
        if (currentOperator == null) {
            throw new Universal403Exception(ErrorCode.INSUFFICIENT_PERMISSIONS.getErrorCod(), "Insufficient Permissions.");
        }
        boolean whiteListUser = false;
        for (String expectedUId : uIdWhiteList) {
            if (expectedUId.equals(currentOperator.getuId())) {
                whiteListUser = true;
                break;
            }
        }
        if (!whiteListUser) {
            if (!currentOperator.getUserType().equals(expectedUserType)) {
                throw new Universal403Exception(ErrorCode.INSUFFICIENT_PERMISSIONS.getErrorCod(), "Insufficient Permissions.");
            }
        }
    }
}