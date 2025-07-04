package cn.edu.hhu.its.user.service.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 用户登录返回参数
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class UserLoginRespDTO {
    /**
     * 用户 ID
     */
    private long userId;

    /**
     * 用户名
     */
    private String username;

    /**
     * Token
     */
    private String accessToken;
}
