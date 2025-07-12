package cn.edu.hhu.spring.boot.starter.common.thread.core;

import cn.edu.hhu.spring.boot.starter.common.thread.config.ThreadPoolProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadPoolExecutor;

@Slf4j
public class ThreadPoolManager implements InitializingBean {
    private final ThreadPoolProperties properties;
    private final Map<String, ThreadPoolExecutor> threadPools = new ConcurrentHashMap<>();
    public ThreadPoolManager(ThreadPoolProperties threadPoolProperties) {
        this.properties = threadPoolProperties;
    }

    @Override
    public void afterPropertiesSet() {
        properties.getExecutors().forEach((name, config) -> {
            ThreadPoolExecutor executor = ThreadPoolExecutorFactory.build(config);
            threadPools.put(name, executor);
            log.info("[线程池初始化] name={} core={} max={}", name, config.getCorePoolSize(), config.getMaxPoolSize());
        });
    }
    public ThreadPoolExecutor get(String name) {
        return threadPools.get(name);
    }

    public Set<String> poolNames() {
        return threadPools.keySet();
    }
}
