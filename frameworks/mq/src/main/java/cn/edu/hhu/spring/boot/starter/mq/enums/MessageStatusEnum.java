package cn.edu.hhu.spring.boot.starter.mq.enums;

import lombok.Getter;

/**
 * 消息状态枚举
 * 
 * @author intelligent-teaching-system
 */
@Getter
public enum MessageStatusEnum {

    /**
     * 发送成功
     */
    SEND_OK("SEND_OK", "发送成功"),

    /**
     * 发送失败
     */
    SEND_FAILED("SEND_FAILED", "发送失败"),

    /**
     * 等待发送
     */
    PENDING("PENDING", "等待发送"),

    /**
     * 消费成功
     */
    CONSUME_SUCCESS("CONSUME_SUCCESS", "消费成功"),

    /**
     * 消费失败
     */
    CONSUME_FAILED("CONSUME_FAILED", "消费失败"),

    /**
     * 重试中
     */
    RETRYING("RETRYING", "重试中"),

    /**
     * 进入死信队列
     */
    DEAD_LETTER("DEAD_LETTER", "进入死信队列"),

    /**
     * 事务提交
     */
    TRANSACTION_COMMIT("TRANSACTION_COMMIT", "事务提交"),

    /**
     * 事务回滚
     */
    TRANSACTION_ROLLBACK("TRANSACTION_ROLLBACK", "事务回滚"),

    /**
     * 事务未知
     */
    TRANSACTION_UNKNOWN("TRANSACTION_UNKNOWN", "事务未知");

    private final String code;
    private final String description;

    MessageStatusEnum(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public static MessageStatusEnum fromCode(String code) {
        for (MessageStatusEnum status : values()) {
            if (status.code.equals(code)) {
                return status;
            }
        }
        return null;
    }
} 