package cn.edu.hhu.its.resource.service.model.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

/**
 * 资源内容编辑请求DTO
 */
@Data
@Schema(description = "资源内容编辑请求DTO")
public class ResourceContentEditRequestDTO {
    
    @Schema(description = "资源ID", required = true)
    private Long resourceId;
    
    @Schema(description = "用户ID", required = true)
    private Long userId;
    
    @Schema(description = "新文件内容")
    private MultipartFile newFile;
    
    @Schema(description = "是否保留旧文件", example = "false")
    private Boolean keepOldFile = false;
} 