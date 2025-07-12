package cn.edu.hhu.its.resource.service.model.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 收藏资源请求DTO
 */
@Data
@Schema(description = "收藏资源请求DTO")
public class ResourceFavoriteRequestDTO {
    
    @Schema(description = "资源ID", required = true)
    private Long resourceId;
    
    @Schema(description = "用户ID", required = true) 
    private Long userId;
    
    @Schema(description = "收藏状态，true为收藏，false为取消收藏", required = true)
    private Boolean favorite;
} 