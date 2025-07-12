package cn.edu.hhu.its.message.service.model.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;
import java.util.Map;

/**
 * 审核记录响应DTO
 *
 * @author ITS项目组
 */
@Data
@Schema(description = "审核记录响应")
public class AuditRecordResponseDTO {

    @Schema(description = "审核记录ID", example = "12345")
    private Long id;

    @Schema(description = "审核类型", example = "RESOURCE")
    private String auditType;

    @Schema(description = "审核目标ID", example = "67890")
    private Long targetId;

    @Schema(description = "目标类型", example = "RESOURCE")
    private String targetType;

    @Schema(description = "提交者ID", example = "11111")
    private Long submitterId;

    @Schema(description = "提交者用户名", example = "user123")
    private String submitterUsername;

    @Schema(description = "审核员ID", example = "22222")
    private Long auditorId;

    @Schema(description = "审核员用户名", example = "auditor123")
    private String auditorUsername;

    @Schema(description = "审核状态：0-待审核，1-审核通过，2-审核拒绝", example = "1")
    private Integer status;

    @Schema(description = "审核状态描述", example = "审核通过")
    private String statusDesc;

    @Schema(description = "审核意见或拒绝原因", example = "资源内容符合要求")
    private String auditReason;

    @Schema(description = "审核相关数据")
    private Map<String, Object> auditData;

    @Schema(description = "提交时间", example = "2024-07-08 10:00:00")
    private Date submittedAt;

    @Schema(description = "审核时间", example = "2024-07-08 11:00:00")
    private Date auditedAt;

    @Schema(description = "创建时间", example = "2024-07-08 10:00:00")
    private Date createdAt;

    @Schema(description = "是否超时", example = "false")
    private Boolean timeout;
}
