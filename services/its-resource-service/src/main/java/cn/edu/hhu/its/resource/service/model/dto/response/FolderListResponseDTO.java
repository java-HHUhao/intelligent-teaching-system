package cn.edu.hhu.its.resource.service.model.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 文件夹列表响应DTO
 */
@Data
@Schema(description = "文件夹列表响应DTO")
public class FolderListResponseDTO {
    
    @Schema(description = "文件夹列表")
    private List<FolderItem> folders;
    
    @Schema(description = "总记录数")
    private Long total;
    
    @Schema(description = "当前页码")
    private Integer pageNum;
    
    @Schema(description = "每页大小")
    private Integer pageSize;
    
    @Data
    @Schema(description = "文件夹项")
    public static class FolderItem {
        @Schema(description = "文件夹ID")
        private Long id;
        
        @Schema(description = "文件夹名称")
        private String folderName;
        
        @Schema(description = "父文件夹ID")
        private Long parentId;
        
        @Schema(description = "创建时间")
        private Date createdAt;
        
        @Schema(description = "更新时间")
        private Date updatedAt;
        
        @Schema(description = "子文件夹数量")
        private Integer subFolderCount;
        
        @Schema(description = "资源数量")
        private Integer resourceCount;
    }
} 