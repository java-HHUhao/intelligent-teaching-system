package cn.edu.hhu.its.user.service.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 用户登录请求参数
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class UserLoginReqDTO {
    /**
     * 用户名
     */
    @NotBlank
    private String usernameOrMailOrPhone;

    /**
     * 密码
     */
    @NotBlank
    private String password;
}
