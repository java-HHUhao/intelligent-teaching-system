package cn.edu.hhu.spring.boot.starter.common.result;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 统一返回结果
 */
@Data
@Accessors(chain = true)
public class Result<T> {
    /**
     * 状态码
     */
    private String code;
    private String message;
    private T data;
}
