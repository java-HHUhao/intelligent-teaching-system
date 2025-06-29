package cn.edu.hhu.its.user.service.model.dto.req;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 用户注册请求参数
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class UserRegisterReqDTO {
    /**
     * 用户名
     */
    @NotBlank
    private String username;
    /**
     * 密码
     */
    @NotBlank
    private String password;
    /**
     * 确认密码
     */
    @NotBlank
    private String confirmPassword;
    /**
     * 邮箱
     */
    @NotBlank(message = "不能为空")
    @Email
    private String email;
    /**
     * 手机号
     */
    @NotBlank(message = "不能为空")
    private String phone;
}
