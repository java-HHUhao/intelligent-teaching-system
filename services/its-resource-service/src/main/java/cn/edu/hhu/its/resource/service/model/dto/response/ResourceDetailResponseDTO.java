package cn.edu.hhu.its.resource.service.model.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

/**
 * 资源详情响应DTO
 */
@Data
@Schema(description = "资源详情响应DTO")
public class ResourceDetailResponseDTO {
    
    @Schema(description = "资源ID")
    private Long id;
    
    @Schema(description = "上传用户ID")
    private Long userId;
    
    @Schema(description = "资源标题")
    private String title;
    
    @Schema(description = "资源类型")
    private String type;
    
    @Schema(description = "资源可见性：0私有，1组共享，2公开")
    private Integer visibility;
    
    @Schema(description = "创建时间")
    private Date createdAt;
    
    @Schema(description = "更新时间")
    private Date updatedAt;
    
    @Schema(description = "文件路径")
    private String filePath;
    
    @Schema(description = "资源描述")
    private String description;
    
    @Schema(description = "扩展元信息")
    private String metadata;
    
    @Schema(description = "文件下载URL")
    private String downloadUrl;
} 