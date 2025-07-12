package cn.edu.hhu.its.resource.service.model.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 组重命名请求DTO
 */
@Data
@Schema(description = "组重命名请求DTO")
public class GroupRenameRequestDTO {
    
    @Schema(description = "组ID", required = true)
    private Long groupId;
    
    @Schema(description = "用户ID", required = true)
    private Long userId;
    
    @Schema(description = "新组名", required = true)
    private String newGroupName;
    
    @Schema(description = "新组描述")
    private String newDescription;
} 