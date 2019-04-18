package org.xavier.blog.article.config;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;
import org.xavier.common.utils.BaseHttpHelper;
import org.xavier.common.utils.HttpHelpper;
import org.xavier.common.utils.UtilsCreator;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;

/**
 * 描述信息：<br/>
 *
 * @author Xavier
 * @version 1.0
 * @date 2019/4/18
 * @since Jdk 1.8
 */
@Configuration
public class CustomBean {
    @Bean
    public HttpHelpper httpHelpper(){
        HttpHelpper baseHttpHelper = new BaseHttpHelper() {
            @Override
            public void initObjectMapper() {
                mapper = new ObjectMapper();
                //反序列化出现多余属性时,选择忽略不抛出异常
                mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                // 开启允许数字以 0 开头
                mapper.configure(JsonParser.Feature.ALLOW_NUMERIC_LEADING_ZEROS, true);
            }

            @Override
           public void initRestTemplate() {
                restTemplate = new RestTemplate();
                ResponseErrorHandler responseErrorHandler = new ResponseErrorHandler() {
                    @Override
                    public boolean hasError(ClientHttpResponse clientHttpResponse) throws IOException {
                        return true;
                    }

                    @Override
                    public void handleError(ClientHttpResponse clientHttpResponse) throws IOException {

                    }
                };
                restTemplate.setErrorHandler(responseErrorHandler);
                List<HttpMessageConverter<?>> converters = restTemplate.getMessageConverters();
                int needReplaceIndex;
                boolean needRemove = false;
                for (needReplaceIndex = 0; needReplaceIndex < converters.size(); needReplaceIndex++) {
                    if (converters.get(needReplaceIndex) instanceof StringHttpMessageConverter) {
                        needRemove = true;
                        break;
                    }
                }
                if (needRemove) {
                    converters.remove(needReplaceIndex);
                    if (needReplaceIndex < converters.size()) {
                        converters.add(needReplaceIndex, new StringHttpMessageConverter(Charset.forName("utf-8")));
                    } else if (needReplaceIndex > -1) {
                        converters.add(needReplaceIndex - 1, new StringHttpMessageConverter(Charset.forName("utf-8")));
                    } else {
                        converters.add(new StringHttpMessageConverter(Charset.forName("utf-8")));
                    }
                    restTemplate.setMessageConverters(converters);
                }
            }
        };
        return baseHttpHelper;
    }
}
