package cn.edu.hhu.its.user.service.model.dto.response;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 修改用户名响应参数
 */
@Data
@Accessors(chain = true)
public class UsernameUpdateRespDTO {
    /**
     * 新用户名
     */
    private String username;

    /**
     * 新token
     */
    private String token;
} 