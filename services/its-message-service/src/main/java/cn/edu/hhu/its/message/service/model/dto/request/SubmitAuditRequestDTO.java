package cn.edu.hhu.its.message.service.model.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Map;

/**
 * 提交审核请求DTO
 * 
 * @author ITS项目组
 */
@Data
@Schema(description = "提交审核请求")
public class SubmitAuditRequestDTO {

    @Schema(description = "审核类型：RESOURCE、USER_AVATAR等", example = "RESOURCE")
    @NotBlank(message = "审核类型不能为空")
    private String auditType;

    @Schema(description = "审核目标ID（资源ID、用户ID等）", example = "12345")
    @NotNull(message = "审核目标ID不能为空")
    private Long targetId;

    @Schema(description = "目标类型：RESOURCE、USER等", example = "RESOURCE")
    @NotBlank(message = "目标类型不能为空")
    private String targetType;

    @Schema(description = "提交者ID", example = "12345")
    @NotNull(message = "提交者ID不能为空")
    private Long submitterId;

    @Schema(description = "审核相关数据")
    private Map<String, Object> auditData;

    @Schema(description = "提交说明", example = "请审核我的资源")
    private String description;
} 