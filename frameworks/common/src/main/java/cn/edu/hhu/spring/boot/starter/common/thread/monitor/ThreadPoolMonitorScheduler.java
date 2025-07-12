package cn.edu.hhu.spring.boot.starter.common.thread.monitor;

import cn.edu.hhu.spring.boot.starter.common.thread.core.ThreadPoolManager;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Slf4j
public class ThreadPoolMonitorScheduler {

    private final ThreadPoolManager threadPoolManager;

    public ThreadPoolMonitorScheduler(ThreadPoolManager threadPoolManager) {
        this.threadPoolManager = threadPoolManager;
        startMonitor();
    }

    private void startMonitor() {
        Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(() -> {
            threadPoolManager.poolNames().forEach(name -> {
                ThreadPoolExecutor executor = threadPoolManager.get(name);
                log.info("[线程池监控] name={} active={} queue={} completed={}",
                        name,
                        executor.getActiveCount(),
                        executor.getQueue().size(),
                        executor.getCompletedTaskCount());
            });
        }, 10, 30, TimeUnit.SECONDS);
    }
}
