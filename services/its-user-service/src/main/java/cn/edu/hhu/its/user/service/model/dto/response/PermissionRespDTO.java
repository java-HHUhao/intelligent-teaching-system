package cn.edu.hhu.its.user.service.model.dto.response;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;
import java.util.List;

/**
 * 权限响应DTO
 */
@Data
@Accessors(chain = true)
public class PermissionRespDTO {
    /**
     * 权限ID
     */
    private Long id;

    /**
     * 权限标识，如 resource:create
     */
    private String code;

    /**
     * 权限名称，中文
     */
    private String name;

    /**
     * 权限类型，如 API、菜单、按钮
     */
    private String type;

    /**
     * 父权限ID
     */
    private Long parentId;

    /**
     * 权限描述
     */
    private String description;

    /**
     * 创建时间
     */
    private Date createdAt;

    /**
     * 更新时间
     */
    private Date updatedAt;

    /**
     * 子权限列表
     */
    private List<PermissionRespDTO> children;
} 