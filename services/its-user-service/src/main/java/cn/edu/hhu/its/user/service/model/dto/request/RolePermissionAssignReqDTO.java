package cn.edu.hhu.its.user.service.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

/**
 * 角色权限分配请求参数
 */
@Data
public class RolePermissionAssignReqDTO {
    /**
     * 角色名称
     */
    @NotBlank(message = "角色名称不能为空")
    private String roleName;

    /**
     * 权限代码列表
     */
    @NotEmpty(message = "权限代码列表不能为空")
    private List<String> permissionCodes;
} 