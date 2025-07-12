package cn.edu.hhu.spring.boot.starter.common.thread.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.HashMap;
import java.util.Map;

@ConfigurationProperties(prefix = "its.thread-pool")
@Data
public class ThreadPoolProperties {
    private Boolean enabled = true;
    /**
     * 多线程池配置，支持命名隔离
     * thread-pool.executors.xx.core-pool-size
     */
    private Map<String, ExecutorConfig> executors = new HashMap<>();
    @Data
    public static class ExecutorConfig {
        private Integer corePoolSize = 4;
        private Integer maxPoolSize = 8;
        private Integer queueCapacity = 50;
        private Integer keepAliveSeconds = 60;
        private String threadNamePrefix = "its-thread-";
    }
}
