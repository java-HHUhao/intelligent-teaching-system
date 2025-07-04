package cn.edu.hhu.its.user.service.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class PermissionUpdateReqDTO {
    /**
     * 权限ID
     */
    @NotNull(message = "权限ID不能为空")
    private Long id;

    /**
     * 权限编码
     */
    @NotBlank(message = "权限编码不能为空")
    private String code;

    /**
     * 权限名称
     */
    @NotBlank(message = "权限名称不能为空")
    private String name;

    /**
     * 权限类型（1：菜单，2：按钮，3：接口）
     */
    @NotNull(message = "权限类型不能为空")
    private String type;

    /**
     * 父权限ID（如果是顶级权限，则为null）
     */
    private Long parentId;

    /**
     * 权限描述
     */
    private String description;
} 