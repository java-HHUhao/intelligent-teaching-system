package cn.edu.hhu.spring.boot.starter.log.config;

import cn.edu.hhu.spring.boot.starter.log.core.LogAspect;
import org.springframework.context.annotation.Bean;

public class LogAutoConfiguration {
    @Bean
    public LogAspect getLogAspect(){return new LogAspect();}
}
