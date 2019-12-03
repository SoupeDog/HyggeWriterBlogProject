package org.xavier.blog.article.service.remote;

import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.xavier.blog.common.ErrorCode;
import org.xavier.blog.common.GatewayResponse;
import org.xavier.common.exception.Universal500RuntimeException;
import org.xavier.common.logging.core.HyggeLogger;
import org.xavier.common.util.http.helper.HttpHelper;
import org.xavier.common.util.http.helper.HttpHelperResponse;

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
    HttpHelper httpHelper;
    @Autowired
    HyggeLogger logger;

    private static final TypeReference RESPONSE_TYPE_REFERENCE_STRING_LIST = new TypeReference<GatewayResponse<ArrayList<String>>>() {
    };

    public ArrayList<String> quarryGroupInfoOfUser(String uId) {
        Long ts=System.currentTimeMillis();
        HttpHelperResponse<GatewayResponse<ArrayList<String>>> response = httpHelper.get(getUserServicePrefix() + "/user-service/main/group/list?uId=" + uId, httpHeaders, RESPONSE_TYPE_REFERENCE_STRING_LIST);
        System.out.println((System.currentTimeMillis()-ts)+" 毫秒=查询用户群组信息");
        if (response.isFail()) {
            throw new Universal500RuntimeException(ErrorCode.REQUEST_FALL_TO_CALL_UPSTREAM_SERVICES.getErrorCod(), "Fall to call User-Service[quarryGroupInfoOfUser].", response.getData().getMsg());
        }
        if (response.getData().getData().size() < 1) {
            return null;
        } else {
            return response.getData().getData();
        }
    }
}