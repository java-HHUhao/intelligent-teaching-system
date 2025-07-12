package cn.edu.hhu.its.resource.service.model.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 资源编辑请求DTO
 */
@Data
@Schema(description = "资源编辑请求DTO")
public class ResourceEditRequestDTO {
    
    @Schema(description = "资源ID", required = true)
    private Long resourceId;
    
    @Schema(description = "用户ID", required = true)
    private Long userId;
    
    @Schema(description = "资源标题")
    private String title;
    
    @Schema(description = "资源描述")
    private String description;
    
    @Schema(description = "资源可见性：0私有，1组共享，2公开")
    private Integer visibility;
    
    @Schema(description = "扩展元信息，JSON格式")
    private String metadata;
} 