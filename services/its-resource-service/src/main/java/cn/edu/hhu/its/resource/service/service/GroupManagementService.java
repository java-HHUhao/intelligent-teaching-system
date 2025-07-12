package cn.edu.hhu.its.resource.service.service;

import cn.edu.hhu.its.resource.service.model.dto.request.*;
import cn.edu.hhu.its.resource.service.model.dto.response.GroupListResponseDTO;
import cn.edu.hhu.its.resource.service.model.dto.response.ResourceListResponseDTO;
import cn.edu.hhu.spring.boot.starter.common.result.Result;

/**
 * 组管理服务接口
 */
public interface GroupManagementService {
    
    /**
     * 创建组
     *
     * @param requestDTO 请求参数
     * @return 操作结果
     */
    Result<Void> createGroup(CreateGroupRequestDTO requestDTO);
    
    /**
     * 获取组列表
     *
     * @param requestDTO 请求参数
     * @return 组列表
     */
    Result<GroupListResponseDTO> getGroupList(GroupListRequestDTO requestDTO);
    
    /**
     * 删除组
     *
     * @param groupId 组ID
     * @param userId  用户ID
     * @return 操作结果
     */
    Result<Void> deleteGroup(Long groupId, Long userId);
    
    /**
     * 重命名组
     *
     * @param requestDTO 请求参数
     * @return 操作结果
     */
    Result<Void> renameGroup(GroupRenameRequestDTO requestDTO);
    
    /**
     * 获取组资源列表
     *
     * @param requestDTO 请求参数
     * @return 组资源列表
     */
    Result<ResourceListResponseDTO> getGroupResourceList(GroupResourceListRequestDTO requestDTO);
    
    /**
     * 为组添加资源
     *
     * @param groupId    组ID
     * @param resourceId 资源ID
     * @param userId     用户ID
     * @return 操作结果
     */
    Result<Void> addResourceToGroup(Long groupId, Long resourceId, Long userId);
    
    /**
     * 从组中删除资源
     *
     * @param groupId    组ID
     * @param resourceId 资源ID
     * @param userId     用户ID
     * @return 操作结果
     */
    Result<Void> removeResourceFromGroup(Long groupId, Long resourceId, Long userId);
    
    /**
     * 邀请用户加入组
     *
     * @param requestDTO 请求参数
     * @return 操作结果
     */
    Result<Void> inviteUsersToGroup(InviteUserToGroupRequestDTO requestDTO);
    
    /**
     * 从组中移除用户
     *
     * @param groupId 组ID
     * @param userId  要移除的用户ID
     * @param operatorId 操作者ID
     * @return 操作结果
     */
    Result<Void> removeUserFromGroup(Long groupId, Long userId, Long operatorId);
} 