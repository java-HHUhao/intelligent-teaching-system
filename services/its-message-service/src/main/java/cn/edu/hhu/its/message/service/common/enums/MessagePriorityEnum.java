package cn.edu.hhu.its.message.service.common.enums;

import lombok.Getter;

/**
 * 消息优先级枚举
 */
@Getter
public enum MessagePriorityEnum {
    NORMAL(1, "普通"),
    IMPORTANT(2, "重要"),
    URGENT(3, "紧急");
    
    private final Integer code;
    private final String description;
    
    MessagePriorityEnum(Integer code, String description) {
        this.code = code;
        this.description = description;
    }

    public static MessagePriorityEnum getByCode(Integer code) {
        for (MessagePriorityEnum priority : values()) {
            if (priority.getCode().equals(code)) {
                return priority;
            }
        }
        return NORMAL;
    }
} 