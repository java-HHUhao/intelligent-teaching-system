package cn.edu.hhu.its.resource.service.model.dto.request;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 组资源列表请求DTO
 */
@Data
@Schema(description = "组资源列表请求DTO")
public class GroupResourceListRequestDTO {

    @Schema(description = "组ID", required = true)
    private Long groupId;

    @Schema(description = "用户ID", required = true)
    private Long userId;

    @Schema(description = "页码，从1开始", example = "1")
    private Integer pageNum = 1;

    @Schema(description = "每页大小", example = "20")
    private Integer pageSize = 20;

    @Schema(description = "搜索关键词")
    private String keyword;

    @Schema(description = "资源类型过滤")
    private String type;
}