package cn.edu.hhu.its.user.service.model.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 更新用户状态请求参数
 */
@Data
@Accessors(chain = true)
public class UserStatusUpdateReqDTO {
    /**
     * 用户ID
     */
    @NotNull(message = "用户ID不能为空")
    private Long userId;

    /**
     * 状态：1-启用，0-禁用
     */
    @NotNull(message = "状态不能为空")
    private Integer status;
} 