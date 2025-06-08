package cn.edu.hhu.spring.boot.starter.cache.impl;

import cn.edu.hhu.spring.boot.starter.cache.service.LockService;
import cn.edu.hhu.spring.boot.starter.common.exception.ServerException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;

import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

/**
 * 使用 Redisson 实现的分布式锁服务
 */
@Slf4j
@RequiredArgsConstructor
public class RedissonLockServiceImpl implements LockService {

    private final RedissonClient redissonClient;

    @Override
    public void lock(String lockKey) {
        try {
            RLock lock = redissonClient.getLock(lockKey);
            lock.lock();
            log.debug("Lock acquired: {}", lockKey);
        } catch (ServerException e) {
            log.error("获取锁失败: {}", lockKey, e);
        }
    }

    @Override
    public boolean tryLock(String lockKey, long waitTime, long leaseTime, TimeUnit unit) {
        try {
            RLock lock = redissonClient.getLock(lockKey);
            boolean locked = lock.tryLock(waitTime, leaseTime, unit);
            log.debug("TryLock {}: {}", lockKey, locked);
            return locked;
        } catch (Exception e) {
            log.error("尝试获取锁失败: {}", lockKey, e);
            return false;
        }
    }

    @Override
    public void unlock(String lockKey) {
        try {
            RLock lock = redissonClient.getLock(lockKey);
            if (lock.isHeldByCurrentThread()) {
                lock.unlock();
                log.debug("Lock released: {}", lockKey);
            }
        } catch (ServerException e) {
            log.error("解锁失败: {}", lockKey, e);
        }
    }

    @Override
    public void executeWithLock(String lockKey, Runnable action) {
        RLock lock = redissonClient.getLock(lockKey);
        try {
            lock.lock();
            log.debug("执行带锁逻辑: {}", lockKey);
            action.run();
        } catch (ServerException e) {
            log.error("执行带锁逻辑失败: {}", lockKey, e);
        } finally {
            if (lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }

    @Override
    public <T> T executeWithLock(String lockKey, Supplier<T> action) {
        RLock lock = redissonClient.getLock(lockKey);
        try {
            lock.lock();
            log.debug("执行带锁逻辑并返回值: {}", lockKey);
            return action.get();
        } catch (ServerException e) {
            log.error("执行带锁逻辑失败: {}", lockKey, e);
            throw new ServerException("分布式锁执行异常");
        } finally {
            if (lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }
    }
}