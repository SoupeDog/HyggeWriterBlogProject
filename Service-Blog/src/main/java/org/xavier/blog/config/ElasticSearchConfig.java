package org.xavier.blog.config;

import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.RestClients;
import org.xavier.blog.config.properties.DateBaseProperties;
import org.xavier.blog.config.properties.ElasticSearchProperties;
import org.xavier.common.logging.core.HyggeLogger;

@Configuration
@EnableConfigurationProperties(ElasticSearchProperties.class)
public class ElasticSearchConfig {
    @Autowired
    ElasticSearchProperties elasticSearchProperties;
    @Autowired
    HyggeLogger logger;

    @Bean
    public RestHighLevelClient client() {
        ClientConfiguration clientConfiguration = ClientConfiguration.builder()
                .connectedTo(elasticSearchProperties.getHost())
                .build();
        logger.always("配置 ES：" + elasticSearchProperties.getHost());
        return RestClients.create(clientConfiguration).rest();
    }
}