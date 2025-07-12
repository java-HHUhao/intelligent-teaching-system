package cn.edu.hhu.its.resource.service.model.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * 邀请用户加入组请求DTO
 */
@Data
@Schema(description = "邀请用户加入组请求DTO")
public class InviteUserToGroupRequestDTO {

    @Schema(description = "组ID", required = true)
    private Long groupId;

    @Schema(description = "邀请者用户ID", required = true)
    private Long inviterUserId;

    @Schema(description = "被邀请的用户ID列表", required = true)
    private List<Long> invitedUserIds;
}