package cn.edu.hhu.its.resource.service.model.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 资源列表响应DTO
 */
@Data
@Schema(description = "资源列表响应DTO")
public class ResourceListResponseDTO {
    
    @Schema(description = "资源列表")
    private List<ResourceItem> resources;
    
    @Schema(description = "文件夹列表")
    private List<FolderItem> folders;
    
    @Schema(description = "总记录数")
    private Long total;
    
    @Schema(description = "当前页码")
    private Integer pageNum;
    
    @Schema(description = "每页大小")
    private Integer pageSize;
    
    @Data
    @Schema(description = "资源项")
    public static class ResourceItem {
        @Schema(description = "资源ID")
        private Long id;
        
        @Schema(description = "资源标题")
        private String title;
        
        @Schema(description = "资源类型")
        private String type;
        
        @Schema(description = "资源可见性")
        private Integer visibility;
        
        @Schema(description = "创建时间")
        private Date createdAt;
        
        @Schema(description = "更新时间")
        private Date updatedAt;
        
        @Schema(description = "文件大小")
        private Long fileSize;
        
        @Schema(description = "文件路径")
        private String filePath;
    }
    
    @Data
    @Schema(description = "文件夹项")
    public static class FolderItem {
        @Schema(description = "文件夹ID")
        private Long id;
        
        @Schema(description = "文件夹名称")
        private String folderName;
        
        @Schema(description = "创建时间")
        private Date createdAt;
        
        @Schema(description = "更新时间")
        private Date updatedAt;
    }
} 