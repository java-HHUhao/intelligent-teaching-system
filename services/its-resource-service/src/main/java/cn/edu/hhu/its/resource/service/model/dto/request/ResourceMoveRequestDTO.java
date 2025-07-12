package cn.edu.hhu.its.resource.service.model.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 资源迁移请求DTO
 */
@Data
@Schema(description = "资源迁移请求DTO")
public class ResourceMoveRequestDTO {
    
    @Schema(description = "资源ID", required = true)
    private Long resourceId;
    
    @Schema(description = "用户ID", required = true)
    private Long userId;
    
    @Schema(description = "目标文件夹ID，null表示移动到根目录")
    private Long targetFolderId;
    
    @Schema(description = "在目标文件夹中的资源显示名")
    private String resourceAlias;
} 