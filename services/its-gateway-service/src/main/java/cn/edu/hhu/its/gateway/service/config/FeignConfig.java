package cn.edu.hhu.its.gateway.service.config;

import feign.Retryer;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Configuration
public class FeignConfig {
    @Bean
    public HttpMessageConverters httpMessageConverters() {
        // 添加多个消息转换器
        List<HttpMessageConverter<?>> converters = new ArrayList<>();
        
        // String转换器
        var stringConverter = new StringHttpMessageConverter(StandardCharsets.UTF_8);
        converters.add(stringConverter);
        
        // Jackson JSON转换器
        var jacksonConverter = new MappingJackson2HttpMessageConverter();
        converters.add(jacksonConverter);
        
        return new HttpMessageConverters(converters);
    }

    /**
     * 配置Feign的重试策略
     * 初始间隔100ms，最大间隔1s，最多重试3次
     */
    @Bean
    public Retryer feignRetryer() {
        return new Retryer.Default(100, TimeUnit.SECONDS.toMillis(1), 3);
    }

    /**
     * 配置负载均衡的RestTemplate
     */
    @Bean
    @LoadBalanced
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
} 