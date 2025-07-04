package cn.edu.hhu.its.user.service.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

/**
 * 修改用户名请求参数
 */
@Data
public class UsernameUpdateReqDTO {
    /**
     * 新用户名
     */
    @NotBlank(message = "用户名不能为空")
    @Pattern(regexp = "^[a-zA-Z0-9_-]{4,16}$", message = "用户名必须为4-16位字母、数字、下划线或中划线")
    private String username;
} 