package cn.edu.hhu.spring.boot.starter.context.config;

import cn.edu.hhu.spring.boot.starter.context.contextholder.ApplicationContextHolder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

public class ApplicationContextAutoConfiguration{
    @Bean
    @ConditionalOnMissingBean
    public ApplicationContextHolder congoApplicationContextHolder() {
        return new ApplicationContextHolder();
    }
}
