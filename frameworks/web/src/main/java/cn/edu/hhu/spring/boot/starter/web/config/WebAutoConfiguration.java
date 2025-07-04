package cn.edu.hhu.spring.boot.starter.web.config;

import cn.edu.hhu.spring.boot.starter.web.globalexception.GlobalExceptionHandler;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

public class WebAutoConfiguration {
    @Bean
    @ConditionalOnMissingBean
    public GlobalExceptionHandler globalExceptionHandler() {return new GlobalExceptionHandler();}
}
