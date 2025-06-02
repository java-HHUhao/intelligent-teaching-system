package cn.edu.hhu.spring.boot.starter.context.config;

import cn.edu.hhu.spring.boot.starter.context.contextholder.ApplicationContextHolder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

public class ApplicationContextAutoConfiguration {
    @Bean
    public ApplicationContextHolder applicationContext() {return new ApplicationContextHolder();}
}
