package cn.edu.hhu.its.resource.service.common.enums;

import cn.edu.hhu.spring.boot.starter.common.enums.BaseErrorCode;
import cn.edu.hhu.spring.boot.starter.common.enums.CommonErrorCode;
import lombok.AllArgsConstructor;

/**
 * 资源服务错误码
 * 错误码格式：RES + 四位数字
 * RES0001 - RES0099：文件操作相关错误
 */
@AllArgsConstructor
public enum ResourceErrorCode implements BaseErrorCode {
    
    // 文件操作相关错误 RES0001 - RES0099
    FILE_EMPTY("RES0001", "文件不能为空"),
    FILE_TYPE_NOT_ALLOWED("RES0002", "不支持的文件类型"),
    FILE_SIZE_EXCEEDED("RES0003", "文件大小超出限制"),
    FILE_UPLOAD_FAILED("RES0004", "文件上传失败"),
    FILE_NOT_FOUND("RES0005", "文件不存在"),
    FILE_DELETE_FAILED("RES0006", "文件删除失败");
    
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
