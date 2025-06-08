package cn.edu.hhu.spring.boot.starter.cache.properties;

import lombok.Data;

@Data
public class LockProperties {

    /**
     * 是否启用分布式锁
     */
    private boolean enabled = true;

    /**
     * 默认获取锁等待时间（秒）
     */
    private long waitTime = 5;

    /**
     * 默认锁租期时间（秒）
     */
    private long leaseTime = 10;
}
