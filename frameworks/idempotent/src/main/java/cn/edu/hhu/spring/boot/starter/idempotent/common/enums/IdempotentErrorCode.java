package cn.edu.hhu.spring.boot.starter.idempotent.common.enums;

import cn.edu.hhu.spring.boot.starter.common.enums.BaseErrorCode;

public enum IdempotentErrorCode implements BaseErrorCode {
    // ========== 二级宏观错误码 系统请求缺少幂等Token ==========
    IDEMPOTENT_TOKEN_NULL_ERROR("A000200", "幂等Token为空"),
    IDEMPOTENT_TOKEN_DELETE_ERROR("A000201", "幂等Token已被使用或失效");
    private final String code;

    private final String message;

    IdempotentErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }
    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
