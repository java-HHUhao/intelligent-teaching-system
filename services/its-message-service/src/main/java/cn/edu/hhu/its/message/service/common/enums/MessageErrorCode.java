package cn.edu.hhu.its.message.service.common.enums;

import cn.edu.hhu.spring.boot.starter.common.enums.BaseErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 消息模块错误码
 * 
 * @author ITS项目组
 */
@Getter
@AllArgsConstructor
public enum MessageErrorCode implements BaseErrorCode {

    // 站内消息相关错误码
    MESSAGE_SEND_ERROR("C000100", "消息发送失败"),
    MESSAGE_NOT_FOUND("C000101", "消息不存在"),
    MESSAGE_ALREADY_READ("C000102", "消息已读"),
    MESSAGE_EXPIRED("C000103", "消息已过期"),
    MESSAGE_SEND_TO_SELF("M000104", "不能发送消息给自己"),
    MESSAGE_BATCH_SIZE_EXCEEDED("C000105", "批量操作数量超出限制"),
    MESSAGE_TEMPLATE_NOT_FOUND("C000106", "消息模板不存在"),
    MESSAGE_TEMPLATE_PARSE_ERROR("C000107", "消息模板解析失败"),

    // 验证码相关错误码
    VERIFICATION_CODE_GENERATE_ERROR("C000200", "验证码生成失败"),
    VERIFICATION_CODE_NOT_FOUND("C000201", "验证码不存在"),
    VERIFICATION_CODE_EXPIRED("C000202", "验证码已过期"),
    VERIFICATION_CODE_USED("C000203", "验证码已使用"),
    VERIFICATION_CODE_INVALID("M000204", "验证码无效"),
    VERIFICATION_CODE_ATTEMPTS_EXCEEDED("C000205", "验证码尝试次数超限"),
    VERIFICATION_CODE_SEND_TOO_FREQUENT("C000206", "验证码发送过于频繁"),
    VERIFICATION_CODE_TYPE_NOT_SUPPORTED("C000207", "不支持的验证码类型"),

    // 审核相关错误码  
    AUDIT_SUBMIT_ERROR("C000300", "审核提交失败"),
    AUDIT_RECORD_NOT_FOUND("C000301", "审核记录不存在"),
    AUDIT_ALREADY_PROCESSED("C000302", "审核已处理"),
    AUDIT_PERMISSION_DENIED("C000303", "无审核权限"),
    AUDIT_CONFIG_NOT_FOUND("C000304", "审核配置不存在"),
    AUDIT_STATUS_INVALID("C000305", "审核状态无效"),
    AUDIT_TIMEOUT("C000306", "审核超时"),

    // 邮件发送相关错误码
    EMAIL_SEND_ERROR("C000400", "邮件发送失败"),
    EMAIL_CONFIG_ERROR("C000401", "邮件配置错误"),
    EMAIL_TEMPLATE_ERROR("M000402", "邮件模板错误"),
    EMAIL_ADDRESS_INVALID("M000403", "邮箱地址无效"),

    // 短信发送相关错误码
    SMS_SEND_ERROR("C000500", "短信发送失败"),
    SMS_CONFIG_ERROR("C000501", "短信配置错误"),
    SMS_TEMPLATE_ERROR("C000502", "短信模板错误"),
    SMS_PHONE_INVALID("C000503", "手机号无效"),

    // 系统相关错误码
    SYSTEM_BUSY("C000600", "系统繁忙，请稍后重试"),
    PARAM_VALIDATION_ERROR("C000601", "参数校验失败"),
    PERMISSION_DENIED("C000602", "权限不足"),
    RATE_LIMIT_EXCEEDED("C000603", "请求频率超限"),
    
    ;

    private final String code;
    private final String message;

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }
} 