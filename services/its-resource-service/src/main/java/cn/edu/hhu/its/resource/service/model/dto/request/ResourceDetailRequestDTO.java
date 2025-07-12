package cn.edu.hhu.its.resource.service.model.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 资源详情请求DTO
 */
@Data
@Schema(description = "资源详情请求DTO")
public class ResourceDetailRequestDTO {
    
    @Schema(description = "资源ID", required = true)
    private Long resourceId;
    
    @Schema(description = "用户ID", required = true)
    private Long userId;
} 