package cn.edu.hhu.spring.boot.starter.log.core;

import lombok.Data;

@Data
public class LogDTO {
    private String beginTime;
    private Object[] inputParams;
    private Object returnValue;
}
