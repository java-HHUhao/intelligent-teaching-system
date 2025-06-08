package cn.edu.hhu.spring.boot.starter.cache.properties;

import lombok.Data;

@Data
public class BloomFilterProperties {

    /**
     * 是否启用布隆过滤器
     */
    private boolean enabled = false;
    /**
     * 布隆过滤器名字
     */
    private String name = "bloomfilter";

    /**
     * 布隆过滤器容量
     */
    private long capacity = 100000;

    /**
     * 误判率
     */
    private double errorRate = 0.01;
}