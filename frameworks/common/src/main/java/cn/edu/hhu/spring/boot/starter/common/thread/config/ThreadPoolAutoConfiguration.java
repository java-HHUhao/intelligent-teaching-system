package cn.edu.hhu.spring.boot.starter.common.thread.config;


import cn.edu.hhu.spring.boot.starter.common.thread.core.ThreadPoolManager;
import cn.edu.hhu.spring.boot.starter.common.thread.monitor.ThreadPoolMonitorScheduler;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

@EnableConfigurationProperties(ThreadPoolProperties.class)
public class ThreadPoolAutoConfiguration {

    @Bean
    public ThreadPoolManager threadPoolManager(ThreadPoolProperties properties) {
        return new ThreadPoolManager(properties);
    }

    @Bean
    @ConditionalOnProperty(prefix = "its.thread-pool", name = "enabled", havingValue = "true")
    public ThreadPoolMonitorScheduler threadPoolMonitorScheduler(ThreadPoolManager manager) {
        return new ThreadPoolMonitorScheduler(manager);
    }
}
