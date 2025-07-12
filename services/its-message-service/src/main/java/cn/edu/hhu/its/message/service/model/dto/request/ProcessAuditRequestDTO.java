package cn.edu.hhu.its.message.service.model.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 审核处理请求DTO
 * 
 * @author ITS项目组
 */
@Data
@Schema(description = "审核处理请求")
public class ProcessAuditRequestDTO {

    @Schema(description = "审核记录ID", example = "12345")
    @NotNull(message = "审核记录ID不能为空")
    private Long auditId;

    @Schema(description = "审核员ID", example = "67890")
    @NotNull(message = "审核员ID不能为空")
    private Long auditorId;

    @Schema(description = "审核状态：1-审核通过，2-审核拒绝", example = "1")
    @NotNull(message = "审核状态不能为空")
    private Integer status;

    @Schema(description = "审核意见或拒绝原因", example = "资源内容符合要求")
    private String auditReason;
} 