package cn.edu.hhu.spring.boot.starter.cache.service;

import java.util.concurrent.TimeUnit;

/**
 * 通用缓存服务接口，封装 Redisson 操作
 */
public interface CacheService {

    /**
     * 设置缓存（有过期时间）
     */
    <T> void set(String key, T value, long timeout, TimeUnit timeUnit);

    /**
     * 设置缓存（默认过期时间）
     */
    <T> void set(String key, T value);

    /**
     * 获取缓存
     */
    <T> T get(String key, Class<T> type);

    /**
     * 删除缓存
     */
    void delete(String key);

    /**
     * 判断缓存是否存在
     */
    boolean hasKey(String key);
}
