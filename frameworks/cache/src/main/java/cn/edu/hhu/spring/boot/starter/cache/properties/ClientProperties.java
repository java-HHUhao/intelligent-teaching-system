package cn.edu.hhu.spring.boot.starter.cache.properties;

import lombok.Data;

@Data
public class ClientProperties {

    /**
     * Redis 连接地址（Redisson 单节点模式）
     * 例如：redis://127.0.0.1:6379
     */
    private String address = "redis://127.0.0.1:6379";

    /**
     * Redis 密码
     */
    private String password;

    /**
     * 连接池大小
     */
    private int connectionPoolSize = 64;

    /**
     * 最小空闲连接数
     */
    private int connectionMinimumIdleSize = 10;
}