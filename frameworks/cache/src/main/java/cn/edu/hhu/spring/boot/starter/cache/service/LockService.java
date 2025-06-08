package cn.edu.hhu.spring.boot.starter.cache.service;

import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

/**
 * 分布式锁服务接口，封装 Redisson 的 RLock
 */
public interface LockService {

    /**
     * 加锁（阻塞等待）
     */
    void lock(String lockKey);

    /**
     * 尝试加锁（非阻塞）
     */
    boolean tryLock(String lockKey, long waitTime, long leaseTime, TimeUnit unit);

    /**
     * 解锁
     */
    void unlock(String lockKey);

    /**
     * 执行带锁的逻辑（无返回值）
     */
    void executeWithLock(String lockKey, Runnable action);

    /**
     * 执行带锁的逻辑（带返回值）
     */
    <T> T executeWithLock(String lockKey, Supplier<T> action);
}