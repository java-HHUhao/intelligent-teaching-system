package cn.edu.hhu.its.resource.service.model.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 文件夹移动请求DTO
 */
@Data
@Schema(description = "文件夹移动请求DTO")
public class FolderMoveRequestDTO {
    
    @Schema(description = "文件夹ID", required = true)
    private Long folderId;
    
    @Schema(description = "用户ID", required = true)
    private Long userId;
    
    @Schema(description = "目标父文件夹ID，null表示移动到根目录")
    private Long targetParentId;
} 