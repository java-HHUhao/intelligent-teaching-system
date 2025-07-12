package cn.edu.hhu.its.resource.service.model.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 文件夹重命名请求DTO
 */
@Data
@Schema(description = "文件夹重命名请求DTO")
public class FolderRenameRequestDTO {
    
    @Schema(description = "文件夹ID", required = true)
    private Long folderId;
    
    @Schema(description = "用户ID", required = true)
    private Long userId;
    
    @Schema(description = "新文件夹名称", required = true)
    private String newFolderName;
} 