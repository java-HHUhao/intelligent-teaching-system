package cn.edu.hhu.its.resource.service.controller;

import cn.edu.hhu.its.resource.service.model.dto.request.*;
import cn.edu.hhu.its.resource.service.model.dto.response.GroupListResponseDTO;
import cn.edu.hhu.its.resource.service.model.dto.response.ResourceListResponseDTO;
import cn.edu.hhu.its.resource.service.service.GroupManagementService;
import cn.edu.hhu.spring.boot.starter.common.result.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 组管理接口 [[memory:2478388]]
 */
@Tag(name = "组管理接口", description = "处理组创建、删除、重命名、成员管理等操作")
@RestController
@RequestMapping("/resource/group")
@RequiredArgsConstructor
public class GroupManagementController {
    
    private final GroupManagementService groupManagementService;
    
    @Operation(summary = "创建组")
    @PostMapping("/create")
    public Result<Void> createGroup(@RequestBody CreateGroupRequestDTO requestDTO) {
        return groupManagementService.createGroup(requestDTO);
    }
    
    @Operation(summary = "获取组列表")
    @PostMapping("/list")
    public Result<GroupListResponseDTO> getGroupList(@RequestBody GroupListRequestDTO requestDTO) {
        return groupManagementService.getGroupList(requestDTO);
    }
    
    @Operation(summary = "删除组")
    @DeleteMapping("/delete")
    public Result<Void> deleteGroup(
            @Parameter(description = "组ID") @RequestParam Long groupId,
            @Parameter(description = "用户ID") @RequestParam Long userId) {
        return groupManagementService.deleteGroup(groupId, userId);
    }
    
    @Operation(summary = "重命名组")
    @PostMapping("/rename")
    public Result<Void> renameGroup(@RequestBody GroupRenameRequestDTO requestDTO) {
        return groupManagementService.renameGroup(requestDTO);
    }
    
    @Operation(summary = "获取组资源列表")
    @PostMapping("/resource/list")
    public Result<ResourceListResponseDTO> getGroupResourceList(@RequestBody GroupResourceListRequestDTO requestDTO) {
        return groupManagementService.getGroupResourceList(requestDTO);
    }
    
    @Operation(summary = "为组添加资源")
    @PostMapping("/resource/add")
    public Result<Void> addResourceToGroup(
            @Parameter(description = "组ID") @RequestParam Long groupId,
            @Parameter(description = "资源ID") @RequestParam Long resourceId,
            @Parameter(description = "用户ID") @RequestParam Long userId) {
        return groupManagementService.addResourceToGroup(groupId, resourceId, userId);
    }
    
    @Operation(summary = "从组中删除资源")
    @DeleteMapping("/resource/remove")
    public Result<Void> removeResourceFromGroup(
            @Parameter(description = "组ID") @RequestParam Long groupId,
            @Parameter(description = "资源ID") @RequestParam Long resourceId,
            @Parameter(description = "用户ID") @RequestParam Long userId) {
        return groupManagementService.removeResourceFromGroup(groupId, resourceId, userId);
    }
    
    @Operation(summary = "邀请用户加入组")
    @PostMapping("/member/invite")
    public Result<Void> inviteUsersToGroup(@RequestBody InviteUserToGroupRequestDTO requestDTO) {
        return groupManagementService.inviteUsersToGroup(requestDTO);
    }
    
    @Operation(summary = "从组中移除用户")
    @DeleteMapping("/member/remove")
    public Result<Void> removeUserFromGroup(
            @Parameter(description = "组ID") @RequestParam Long groupId,
            @Parameter(description = "要移除的用户ID") @RequestParam Long userId,
            @Parameter(description = "操作者ID") @RequestParam Long operatorId) {
        return groupManagementService.removeUserFromGroup(groupId, userId, operatorId);
    }
} 