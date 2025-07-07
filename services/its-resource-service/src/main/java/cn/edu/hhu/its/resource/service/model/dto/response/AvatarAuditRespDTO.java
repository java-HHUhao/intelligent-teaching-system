package cn.edu.hhu.its.resource.service.model.dto.response;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 头像审核响应参数
 */
@Data
@Accessors(chain = true)
public class AvatarAuditRespDTO {
    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 审核状态：1-通过，2-驳回
     */
    private Integer status;

    /**
     * 驳回原因
     */
    private String reason;

    /**
     * 正式头像URL（审核通过时返回）
     */
    private String avatarUrl;
} 