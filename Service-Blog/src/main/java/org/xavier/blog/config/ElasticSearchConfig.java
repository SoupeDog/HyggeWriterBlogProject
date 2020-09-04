package org.xavier.blog.config;

import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.RestClients;

@Configuration
public class ElasticSearchConfig {

    @Bean
    public RestHighLevelClient client(){
        ClientConfiguration clientConfiguration = ClientConfiguration.builder()
                .connectedTo("123.206.96.81:9200")
                .build();
        return RestClients.create(clientConfiguration).rest();
    }
}