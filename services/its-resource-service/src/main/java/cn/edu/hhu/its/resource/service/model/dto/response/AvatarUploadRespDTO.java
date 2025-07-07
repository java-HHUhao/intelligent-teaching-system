package cn.edu.hhu.its.resource.service.model.dto.response;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 头像上传响应参数
 */
@Data
@Accessors(chain = true)
public class AvatarUploadRespDTO {
    /**
     * 临时头像URL
     */
    private String tempUrl;

    /**
     * 审核状态：0-未提交，1-待审核，2-审核通过，3-驳回
     */
    private Integer auditStatus;
} 