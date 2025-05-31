package cn.edu.hhu.spring.boot.starter.common.result;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;

@Data
@Accessors(chain = true)
public class Result<T> implements Serializable {
    @Serial
    private static final long serialVersionUID = 7122965463013407402L;
    private String code;
    private String message;
    private T data;
}
