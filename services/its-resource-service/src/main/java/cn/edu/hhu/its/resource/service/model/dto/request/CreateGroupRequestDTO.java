package cn.edu.hhu.its.resource.service.model.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 创建组请求DTO
 */
@Data
@Schema(description = "创建组请求DTO")
public class CreateGroupRequestDTO {
    
    @Schema(description = "组名称", required = true)
    private String groupName;
    
    @Schema(description = "组描述")
    private String description;
    
    @Schema(description = "创建者用户ID", required = true)
    private Long createUser;
} 