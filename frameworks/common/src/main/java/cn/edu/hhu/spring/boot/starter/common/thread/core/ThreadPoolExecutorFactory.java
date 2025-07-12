package cn.edu.hhu.spring.boot.starter.common.thread.core;

import cn.edu.hhu.spring.boot.starter.common.thread.config.ThreadPoolProperties;
import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadPoolExecutorFactory {
    public static ThreadPoolExecutor build(ThreadPoolProperties.ExecutorConfig config) {
        return new ThreadPoolExecutor(
                config.getCorePoolSize(),
                config.getMaxPoolSize(),
                config.getKeepAliveSeconds(),
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(config.getQueueCapacity()),
                new ThreadFactoryBuilder()
                        .setNameFormat(config.getThreadNamePrefix() + "%d")
                        .setDaemon(true)
                        .build(),
                new ThreadPoolExecutor.CallerRunsPolicy()
        );
    }
}
