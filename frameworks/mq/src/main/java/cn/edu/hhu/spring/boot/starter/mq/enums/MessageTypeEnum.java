package cn.edu.hhu.spring.boot.starter.mq.enums;

import lombok.Getter;

/**
 * 消息类型枚举
 * 
 * @author intelligent-teaching-system
 */
@Getter
public enum MessageTypeEnum {

    /**
     * 普通消息
     */
    NORMAL("NORMAL", "普通消息"),

    /**
     * 延迟消息
     */
    DELAY("DELAY", "延迟消息"),

    /**
     * 定时消息
     */
    SCHEDULED("SCHEDULED", "定时消息"),

    /**
     * 事务消息
     */
    TRANSACTION("TRANSACTION", "事务消息"),

    /**
     * 顺序消息
     */
    ORDERLY("ORDERLY", "顺序消息"),

    /**
     * 死信消息
     */
    DEAD_LETTER("DEAD_LETTER", "死信消息"),

    /**
     * 重试消息
     */
    RETRY("RETRY", "重试消息");

    private final String code;
    private final String description;

    MessageTypeEnum(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public static MessageTypeEnum fromCode(String code) {
        for (MessageTypeEnum type : values()) {
            if (type.code.equals(code)) {
                return type;
            }
        }
        return NORMAL; // 默认普通消息
    }
} 