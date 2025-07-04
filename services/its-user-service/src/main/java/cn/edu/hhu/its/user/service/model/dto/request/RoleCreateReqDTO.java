package cn.edu.hhu.its.user.service.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 创建角色请求参数
 */
@Data
@Accessors(chain = true)
public class RoleCreateReqDTO {
    /**
     * 角色名称
     * 只能包含大写字母、数字和下划线，如：ADMIN、TEACHER_ADMIN
     */
    @NotBlank(message = "角色名称不能为空")
    @Pattern(regexp = "^[A-Z0-9_]+$", message = "角色名称只能包含大写字母、数字和下划线")
    @Size(min = 2, max = 64, message = "角色名称长度必须在2-64个字符之间")
    private String name;

    /**
     * 角色描述
     */
    @Size(max = 255, message = "角色描述不能超过255个字符")
    private String description;
}
