package cn.edu.hhu.its.message.service.common.enums;

import lombok.Getter;

/**
 * 审核状态枚举
 */
@Getter
public enum AuditStatusEnum {
    PENDING(0, "待审核"),
    APPROVED(1, "审核通过"),
    REJECTED(2, "审核拒绝");
    
    private final Integer code;
    private final String description;
    
    AuditStatusEnum(Integer code, String description) {
        this.code = code;
        this.description = description;
    }

    public static AuditStatusEnum getByCode(Integer code) {
        for (AuditStatusEnum status : values()) {
            if (status.getCode().equals(code)) {
                return status;
            }
        }
        return null;
    }
} 