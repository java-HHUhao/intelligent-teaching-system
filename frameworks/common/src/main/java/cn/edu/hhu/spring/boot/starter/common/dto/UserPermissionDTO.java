package cn.edu.hhu.spring.boot.starter.common.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/**
 * 用户权限DTO
 */
@Data
@Accessors(chain = true)
public class UserPermissionDTO implements Serializable {
    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 用户名
     */
    private String username;

    /**
     * 权限编码列表
     */
    private List<String> permissions;
} 