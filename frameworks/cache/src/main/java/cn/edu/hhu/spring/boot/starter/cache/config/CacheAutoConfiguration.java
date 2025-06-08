package cn.edu.hhu.spring.boot.starter.cache.config;

import cn.edu.hhu.spring.boot.starter.cache.impl.RedissonCacheServiceImpl;
import cn.edu.hhu.spring.boot.starter.cache.impl.RedissonLockServiceImpl;
import cn.edu.hhu.spring.boot.starter.cache.properties.BloomFilterProperties;
import cn.edu.hhu.spring.boot.starter.cache.properties.CacheProperties;
import cn.edu.hhu.spring.boot.starter.cache.service.CacheService;
import cn.edu.hhu.spring.boot.starter.cache.service.LockService;
import lombok.extern.slf4j.Slf4j;
import org.redisson.Redisson;
import org.redisson.RedissonBloomFilter;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.redisson.config.SingleServerConfig;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

@Slf4j
@EnableConfigurationProperties(CacheProperties.class)
public class CacheAutoConfiguration {

    /**
     * 注入 RedissonClient（单机模式）
     */
    @Bean
    @ConditionalOnMissingBean
    public RedissonClient redissonClient(CacheProperties cacheProperties) {
        log.info("[CacheAutoConfiguration] 初始化 RedissonClient...");

        Config config = new Config();
        SingleServerConfig serverConfig = config.useSingleServer()
                .setAddress(cacheProperties.getClient().getAddress())
                .setConnectionPoolSize(cacheProperties.getClient().getConnectionPoolSize())
                .setConnectionMinimumIdleSize(cacheProperties.getClient().getConnectionMinimumIdleSize());

        if (cacheProperties.getClient().getPassword() != null) {
            serverConfig.setPassword(cacheProperties.getClient().getPassword());
        }

        return Redisson.create(config);
    }

    /**
     * 注入缓存服务
     */
    @Bean
    @ConditionalOnMissingBean
    public CacheService cacheService(RedissonClient redissonClient, CacheProperties cacheProperties) {
        return new RedissonCacheServiceImpl(redissonClient);
    }

    /**
     * 注入分布式锁服务
     */
    @Bean
    @ConditionalOnMissingBean
    public LockService lockService(RedissonClient redissonClient, CacheProperties cacheProperties) {
        return new RedissonLockServiceImpl(redissonClient);
    }

    /**
     * 防止缓存穿透的布隆过滤器
     */
    @Bean
    @ConditionalOnProperty(prefix = "its-cache.bloom", name = "enabled", havingValue = "true")
    public RBloomFilter<String> cachePenetrationBloomFilter(RedissonClient redissonClient, BloomFilterProperties bloomFilterProperties) {
        RBloomFilter<String> cachePenetrationBloomFilter = redissonClient.getBloomFilter(bloomFilterProperties.getName());
        cachePenetrationBloomFilter.tryInit(bloomFilterProperties.getCapacity(), bloomFilterProperties.getErrorRate());
        return cachePenetrationBloomFilter;
    }
}
