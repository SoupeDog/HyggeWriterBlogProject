package org.xavier.blog.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 描述信息：<br/>
 *
 * @author Xavier
 * @version 1.0
 * @date 2018/2/8
 * @since Jdk 1.8
 */
@ConfigurationProperties(prefix = "es")
public class ElasticSearchProperties {
    private String host;

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }
}