package cn.edu.hhu.spring.boot.starter.cache.impl;


import cn.edu.hhu.spring.boot.starter.cache.service.CacheService;
import cn.edu.hhu.spring.boot.starter.common.exception.ServerException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Value;

import java.util.concurrent.TimeUnit;

/**
 * 使用 Redisson 封装的缓存实现类
 */
@Slf4j
@RequiredArgsConstructor
public class RedissonCacheServiceImpl implements CacheService {

    private final RedissonClient redissonClient;

    @Value("${cache.default-ttl:3600}")
    private long defaultTtlInSeconds;

    @Override
    public <T> void set(String key, T value, long timeout, TimeUnit timeUnit) {
        try {
            RBucket<T> bucket = redissonClient.getBucket(key);
            bucket.set(value, timeout, timeUnit);
        } catch (ServerException e) {
            log.error("Redis 缓存设置失败 key={} value={}", key, value, e);
        }
    }

    @Override
    public <T> void set(String key, T value) {
        set(key, value, defaultTtlInSeconds, TimeUnit.SECONDS);
    }

    @Override
    public <T> T get(String key, Class<T> type) {
        try {
            RBucket<T> bucket = redissonClient.getBucket(key);
            return bucket.get();
        } catch (ServerException e) {
            log.error("Redis 获取缓存失败 key={}", key, e);
            return null;
        }
    }

    @Override
    public void delete(String key) {
        try {
            redissonClient.getBucket(key).delete();
        } catch (ServerException e) {
            log.error("Redis 删除缓存失败 key={}", key, e);
        }
    }

    @Override
    public boolean hasKey(String key) {
        try {
            return redissonClient.getBucket(key).isExists();
        } catch (ServerException e) {
            log.error("Redis 查询 key 是否存在失败 key={}", key, e);
            return false;
        }
    }
}
