package cn.edu.hhu.spring.boot.starter.mq.enums;

import lombok.Getter;

/**
 * 消费模式枚举
 * 
 * @author intelligent-teaching-system
 */
@Getter
public enum ConsumerModeEnum {

    /**
     * 同步消费
     */
    SYNC("SYNC", "同步消费"),

    /**
     * 异步消费
     */
    ASYNC("ASYNC", "异步消费"),

    /**
     * 批量消费
     */
    BATCH("BATCH", "批量消费"),

    /**
     * 顺序消费
     */
    ORDERLY("ORDERLY", "顺序消费"),

    /**
     * 并发消费
     */
    CONCURRENTLY("CONCURRENTLY", "并发消费");

    private final String code;
    private final String description;

    ConsumerModeEnum(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public static ConsumerModeEnum fromCode(String code) {
        for (ConsumerModeEnum mode : values()) {
            if (mode.code.equals(code)) {
                return mode;
            }
        }
        return ASYNC; // 默认异步消费
    }
} 