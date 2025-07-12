package cn.edu.hhu.its.resource.service.model.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 创建文件夹请求DTO
 */
@Data
@Schema(description = "创建文件夹请求DTO")
public class CreateFolderRequestDTO {
    
    @Schema(description = "用户ID", required = true)
    private Long userId;
    
    @Schema(description = "父文件夹ID，null表示在根目录下创建")
    private Long parentId;
    
    @Schema(description = "文件夹名称", required = true)
    private String folderName;
} 