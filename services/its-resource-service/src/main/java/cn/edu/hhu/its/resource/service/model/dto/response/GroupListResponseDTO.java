package cn.edu.hhu.its.resource.service.model.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 组列表响应DTO
 */
@Data
@Schema(description = "组列表响应DTO")
public class GroupListResponseDTO {
    
    @Schema(description = "组列表")
    private List<GroupItem> groups;
    
    @Schema(description = "总记录数")
    private Long total;
    
    @Schema(description = "当前页码")
    private Integer pageNum;
    
    @Schema(description = "每页大小")
    private Integer pageSize;
    
    @Data
    @Schema(description = "组项")
    public static class GroupItem {
        @Schema(description = "组ID")
        private Long id;
        
        @Schema(description = "组名称")
        private String groupName;
        
        @Schema(description = "组描述")
        private String description;
        
        @Schema(description = "创建者ID")
        private Long createUser;
        
        @Schema(description = "创建时间")
        private Date createdAt;
        
        @Schema(description = "成员数量")
        private Integer memberCount;
        
        @Schema(description = "是否为创建者")
        private Boolean isCreator;
    }
} 