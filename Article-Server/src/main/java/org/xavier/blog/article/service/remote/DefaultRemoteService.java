package org.xavier.blog.article.service.remote;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.xavier.common.utils.HttpHelpper;
import org.xavier.web.extend.DefaultService;

/**
 * 描述信息：<br/>
 *
 * @author Xavier
 * @version 1.0
 * @date 2019/5/7
 * @since Jdk 1.8
 */
@Service
public class DefaultRemoteService extends DefaultService {
    @Value("${remote.user.service.name}")
    protected volatile String USER_SERVICE_NAME;
    @Autowired
    protected LoadBalancerClient client;
    @Autowired
    protected HttpHelpper httpHelpper;

    protected static HttpHeaders httpHeaders = new HttpHeaders() {{

    }};


    protected String getUserServicePrefix() {
        return "http://127.0.0.1:8080";
    }
}