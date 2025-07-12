package cn.edu.hhu.its.message.service.common.enums;

import lombok.Getter;

/**
 * 消息类型枚举（与数据库message_type表对应）
 */
@Getter
public enum MessageTypeEnum {
    SYSTEM_NOTICE("系统通知", "系统级别的通知消息"),
    VERIFICATION_CODE("验证码", "验证码类消息"),
    SYSTEM_ALERT("系统告警", "系统异常、服务宕机、监控告警等");
    
    private final String typeName;
    private final String description;
    
    MessageTypeEnum(String typeName, String description) {
        this.typeName = typeName;
        this.description = description;
    }

    public String getTypeCode() {
        return this.name();
    }
}

