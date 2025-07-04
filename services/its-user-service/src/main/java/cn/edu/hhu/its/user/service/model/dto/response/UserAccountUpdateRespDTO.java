package cn.edu.hhu.its.user.service.model.dto.response;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 用户账户信息更新响应参数
 */
@Data
@Accessors(chain = true)
public class UserAccountUpdateRespDTO {
    /**
     * 邮箱
     */
    private String email;

    /**
     * 手机号
     */
    private String phone;
} 