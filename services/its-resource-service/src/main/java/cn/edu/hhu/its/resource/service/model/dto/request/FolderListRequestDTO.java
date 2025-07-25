package cn.edu.hhu.its.resource.service.model.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 文件夹列表请求DTO
 */
@Data
@Schema(description = "文件夹列表请求DTO")
public class FolderListRequestDTO {
    
    @Schema(description = "用户ID", required = true)
    private Long userId;
    
    @Schema(description = "父文件夹ID，null表示根目录")
    private Long parentId;
    
    @Schema(description = "页码，从1开始", example = "1")
    private Integer pageNum = 1;
    
    @Schema(description = "每页大小", example = "20")
    private Integer pageSize = 20;
    
    @Schema(description = "搜索关键词")
    private String keyword;
} 