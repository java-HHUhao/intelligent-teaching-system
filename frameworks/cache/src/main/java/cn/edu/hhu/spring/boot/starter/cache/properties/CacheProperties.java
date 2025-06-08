package cn.edu.hhu.spring.boot.starter.cache.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "its-cache")
public class CacheProperties {

    /**
     * 是否启用缓存总开关
     */
    private boolean enabled = true;

    /**
     * 默认缓存 TTL（秒）
     */
    private long defaultTtl = 3600;

    /**
     * 布隆过滤器配置
     */
    private BloomFilterProperties bloom = new BloomFilterProperties();

    /**
     * 分布式锁配置
     */
    private LockProperties lock = new LockProperties();

    /**
     * Redisson 客户端连接配置
     */
    private ClientProperties client = new ClientProperties();
}