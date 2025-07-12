package cn.edu.hhu.its.resource.service.model.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

/**
 * 文件夹信息响应DTO
 */
@Data
@Schema(description = "文件夹信息响应DTO")
public class FolderInfoResponseDTO {
    
    @Schema(description = "文件夹ID")
    private Long id;
    
    @Schema(description = "所属用户ID")
    private Long userId;
    
    @Schema(description = "父文件夹ID")
    private Long parentId;
    
    @Schema(description = "文件夹名称")
    private String folderName;
    
    @Schema(description = "创建时间")
    private Date createdAt;
    
    @Schema(description = "更新时间")
    private Date updatedAt;
} 