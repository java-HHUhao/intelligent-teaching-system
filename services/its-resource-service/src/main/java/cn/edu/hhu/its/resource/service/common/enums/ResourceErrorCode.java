package cn.edu.hhu.its.resource.service.common.enums;

import cn.edu.hhu.spring.boot.starter.common.enums.BaseErrorCode;

/**
 * 资源模块错误码
 */
public enum ResourceErrorCode implements BaseErrorCode {
    
    FILE_EMPTY("R0001", "文件不能为空"),
    FILE_TYPE_NOT_ALLOWED("R0002", "不支持的文件类型"),
    FILE_UPLOAD_FAILED("R0003", "文件上传失败"),
    FILE_NOT_FOUND("R0004", "文件不存在"),
    FILE_DELETE_FAILED("R0005", "文件删除失败"),
    
    RESOURCE_NOT_FOUND("R1001", "资源不存在"),
    RESOURCE_ACCESS_DENIED("R1002", "没有访问权限"),
    RESOURCE_DELETE_FAILED("R1003", "资源删除失败"),
    RESOURCE_MOVE_FAILED("R1004", "资源移动失败"),
    RESOURCE_RENAME_FAILED("R1005", "资源重命名失败"),
    
    FOLDER_NOT_FOUND("R2001", "文件夹不存在"),
    FOLDER_ALREADY_EXISTS("R2002", "文件夹已存在"),
    FOLDER_NOT_EMPTY("R2003", "文件夹不为空"),
    FOLDER_CREATE_FAILED("R2004", "文件夹创建失败"),
    FOLDER_DELETE_FAILED("R2005", "文件夹删除失败"),
    FOLDER_RENAME_FAILED("R2006", "文件夹重命名失败"),
    
    INVALID_PARAMETER("R9001", "参数错误"),
    OPERATION_FAILED("R9999", "操作失败");
    
    private final String code;
    private final String message;
    
    ResourceErrorCode(String code, String message) {
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
