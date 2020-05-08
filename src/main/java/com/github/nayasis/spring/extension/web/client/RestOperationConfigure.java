package com.github.nayasis.spring.extension.web.client;

import com.github.nayasis.spring.extension.web.client.interceptor.RestTemplateLoggingInterceptor;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestOperations;

import java.time.Duration;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * @author nayasis@gmail.com
 * @since 2018-03-12
 */
@Configuration
public class RestOperationConfigure {

    @Bean
    public RestOperations restOperations( RestTemplateBuilder restTemplateBuilder ) {
        return restTemplateBuilder
            .requestFactory( () -> new BufferingClientHttpRequestFactory(new SimpleClientHttpRequestFactory()) )
            .setConnectTimeout( Duration.ofSeconds(3L) )
            .setReadTimeout( Duration.ofSeconds(600L) )
            .additionalMessageConverters( new StringHttpMessageConverter(UTF_8) )
            .additionalInterceptors( new RestTemplateLoggingInterceptor() )
            .build();
    }

}
