package cn.edu.hhu.its.resource.service.service.impl;

import cn.edu.hhu.its.resource.service.common.enums.ResourceErrorCode;
import cn.edu.hhu.its.resource.service.model.domain.*;
import cn.edu.hhu.its.resource.service.model.dto.request.*;
import cn.edu.hhu.its.resource.service.model.dto.response.GroupListResponseDTO;
import cn.edu.hhu.its.resource.service.model.dto.response.ResourceListResponseDTO;
import cn.edu.hhu.its.resource.service.model.mapper.*;
import cn.edu.hhu.its.resource.service.service.GroupManagementService;
import cn.edu.hhu.spring.boot.starter.common.result.Result;
import cn.edu.hhu.spring.boot.starter.common.utils.ResultUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 组管理服务实现类 [[memory:2478388]]
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class GroupManagementServiceImpl implements GroupManagementService {
    
    private final UserGroupMapper userGroupMapper;
    private final UserGroupMappingMapper userGroupMappingMapper;
    private final GroupResourceMapper groupResourceMapper;
    private final ResourceMapper resourceMapper;
    private final ResourceDetailMapper resourceDetailMapper;
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<Void> createGroup(CreateGroupRequestDTO requestDTO) {
        try {
            // 检查组名是否已存在
            LambdaQueryWrapper<UserGroupDO> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(UserGroupDO::getGroupName, requestDTO.getGroupName());
            UserGroupDO existingGroup = userGroupMapper.selectOne(wrapper);
            if (existingGroup != null) {
                return ResultUtil.fail(ResourceErrorCode.FOLDER_ALREADY_EXISTS);
            }
            
            // 创建组
            UserGroupDO group = new UserGroupDO();
            group.setGroupName(requestDTO.getGroupName());
            group.setDescription(requestDTO.getDescription());
            group.setCreateUser(requestDTO.getCreateUser());
            group.setCreatedAt(new Date());
            
            userGroupMapper.insert(group);
            
            // 将创建者加入组
            UserGroupMappingDO mapping = new UserGroupMappingDO();
            mapping.setUserId(requestDTO.getCreateUser());
            mapping.setGroupId(group.getId());
            mapping.setJoinedAt(new Date());
            
            userGroupMappingMapper.insert(mapping);
            
            return ResultUtil.success();
        } catch (Exception e) {
            log.error("创建用户组失败", e);
            return ResultUtil.fail(ResourceErrorCode.OPERATION_FAILED);
        }
    }
    
    @Override
    public Result<GroupListResponseDTO> getGroupList(GroupListRequestDTO requestDTO) {
        try {
            // 查询用户所属的组ID列表
            List<UserGroupMappingDO> mappings = userGroupMappingMapper.selectList(
                    new LambdaQueryWrapper<UserGroupMappingDO>()
                            .eq(UserGroupMappingDO::getUserId, requestDTO.getUserId())
            );
            
            if (mappings.isEmpty()) {
                return ResultUtil.success(new GroupListResponseDTO());
            }
            
            List<Long> groupIds = mappings.stream()
                    .map(UserGroupMappingDO::getGroupId)
                    .collect(Collectors.toList());
            
            // 构建查询条件
            LambdaQueryWrapper<UserGroupDO> wrapper = new LambdaQueryWrapper<>();
            wrapper.in(UserGroupDO::getId, groupIds);
            
            if (StringUtils.hasText(requestDTO.getKeyword())) {
                wrapper.like(UserGroupDO::getGroupName, requestDTO.getKeyword());
            }
            
            wrapper.orderByDesc(UserGroupDO::getCreatedAt);
            
            // 分页查询
            Page<UserGroupDO> page = new Page<>(requestDTO.getPageNum(), requestDTO.getPageSize());
            IPage<UserGroupDO> result = userGroupMapper.selectPage(page, wrapper);
            
            // 转换为响应DTO
            GroupListResponseDTO responseDTO = new GroupListResponseDTO();
            responseDTO.setTotal(result.getTotal());
            responseDTO.setPageNum(requestDTO.getPageNum());
            responseDTO.setPageSize(requestDTO.getPageSize());
            
            List<GroupListResponseDTO.GroupItem> groupItems = result.getRecords().stream()
                    .map(group -> convertToGroupItem(group, requestDTO.getUserId()))
                    .collect(Collectors.toList());
            responseDTO.setGroups(groupItems);
            
            return ResultUtil.success(responseDTO);
        } catch (Exception e) {
            log.error("获取组列表失败", e);
            return ResultUtil.fail(ResourceErrorCode.OPERATION_FAILED);
        }
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<Void> deleteGroup(Long groupId, Long userId) {
        try {
            // 查询组
            UserGroupDO group = userGroupMapper.selectById(groupId);
            if (group == null) {
                return ResultUtil.fail(ResourceErrorCode.FOLDER_NOT_FOUND);
            }
            
            // 权限检查：只有创建者可以删除组
            if (!Objects.equals(group.getCreateUser(), userId)) {
                return ResultUtil.fail(ResourceErrorCode.RESOURCE_ACCESS_DENIED);
            }
            
            // 删除组资源关系
            groupResourceMapper.delete(
                    new LambdaQueryWrapper<GroupResourceDO>()
                            .eq(GroupResourceDO::getGroupId, groupId)
            );
            
            // 删除用户组映射关系
            userGroupMappingMapper.delete(
                    new LambdaQueryWrapper<UserGroupMappingDO>()
                            .eq(UserGroupMappingDO::getGroupId, groupId)
            );
            
            // 删除组
            userGroupMapper.deleteById(groupId);
            
            return ResultUtil.success();
        } catch (Exception e) {
            log.error("删除组失败", e);
            return ResultUtil.fail(ResourceErrorCode.OPERATION_FAILED);
        }
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<Void> renameGroup(GroupRenameRequestDTO requestDTO) {
        try {
            // 查询组
            UserGroupDO group = userGroupMapper.selectById(requestDTO.getGroupId());
            if (group == null) {
                return ResultUtil.fail(ResourceErrorCode.FOLDER_NOT_FOUND);
            }
            
            // 权限检查：只有创建者可以重命名组
            if (!Objects.equals(group.getCreateUser(), requestDTO.getUserId())) {
                return ResultUtil.fail(ResourceErrorCode.RESOURCE_ACCESS_DENIED);
            }
            
            // 检查新组名是否已存在
            LambdaQueryWrapper<UserGroupDO> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(UserGroupDO::getGroupName, requestDTO.getNewGroupName())
                   .ne(UserGroupDO::getId, requestDTO.getGroupId());
            
            UserGroupDO existingGroup = userGroupMapper.selectOne(wrapper);
            if (existingGroup != null) {
                return ResultUtil.fail(ResourceErrorCode.FOLDER_ALREADY_EXISTS);
            }
            
            // 更新组信息
            group.setGroupName(requestDTO.getNewGroupName());
            if (StringUtils.hasText(requestDTO.getNewDescription())) {
                group.setDescription(requestDTO.getNewDescription());
            }
            
            userGroupMapper.updateById(group);
            
            return ResultUtil.success();
        } catch (Exception e) {
            log.error("重命名组失败", e);
            return ResultUtil.fail(ResourceErrorCode.OPERATION_FAILED);
        }
    }
    
    @Override
    public Result<ResourceListResponseDTO> getGroupResourceList(GroupResourceListRequestDTO requestDTO) {
        try {
            // 验证用户是否属于该组
            if (!isUserInGroup(requestDTO.getUserId(), requestDTO.getGroupId())) {
                return ResultUtil.fail(ResourceErrorCode.RESOURCE_ACCESS_DENIED);
            }
            
            // 查询组关联的资源ID列表
            List<GroupResourceDO> groupResources = groupResourceMapper.selectList(
                    new LambdaQueryWrapper<GroupResourceDO>()
                            .eq(GroupResourceDO::getGroupId, requestDTO.getGroupId())
            );
            
            if (groupResources.isEmpty()) {
                return ResultUtil.success(new ResourceListResponseDTO());
            }
            
            List<Long> resourceIds = groupResources.stream()
                    .map(GroupResourceDO::getResourceId)
                    .collect(Collectors.toList());
            
            // 构建查询条件
            LambdaQueryWrapper<ResourceDO> wrapper = new LambdaQueryWrapper<>();
            wrapper.in(ResourceDO::getId, resourceIds);
            
            if (StringUtils.hasText(requestDTO.getKeyword())) {
                wrapper.like(ResourceDO::getTitle, requestDTO.getKeyword());
            }
            
            if (StringUtils.hasText(requestDTO.getType())) {
                wrapper.eq(ResourceDO::getType, requestDTO.getType());
            }
            
            wrapper.orderByDesc(ResourceDO::getUpdatedAt);
            
            // 分页查询
            Page<ResourceDO> page = new Page<>(requestDTO.getPageNum(), requestDTO.getPageSize());
            IPage<ResourceDO> result = resourceMapper.selectPage(page, wrapper);
            
            // 转换为响应DTO
            ResourceListResponseDTO responseDTO = new ResourceListResponseDTO();
            responseDTO.setTotal(result.getTotal());
            responseDTO.setPageNum(requestDTO.getPageNum());
            responseDTO.setPageSize(requestDTO.getPageSize());
            
            List<ResourceListResponseDTO.ResourceItem> resourceItems = result.getRecords().stream()
                    .map(this::convertToResourceItem)
                    .collect(Collectors.toList());
            responseDTO.setResources(resourceItems);
            
            return ResultUtil.success(responseDTO);
        } catch (Exception e) {
            log.error("获取组资源列表失败", e);
            return ResultUtil.fail(ResourceErrorCode.OPERATION_FAILED);
        }
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<Void> addResourceToGroup(Long groupId, Long resourceId, Long userId) {
        try {
            // 验证用户是否属于该组
            if (!isUserInGroup(userId, groupId)) {
                return ResultUtil.fail(ResourceErrorCode.RESOURCE_ACCESS_DENIED);
            }
            
            // 验证资源是否存在
            ResourceDO resource = resourceMapper.selectById(resourceId);
            if (resource == null) {
                return ResultUtil.fail(ResourceErrorCode.RESOURCE_NOT_FOUND);
            }
            
            // 检查资源是否已在组中
            GroupResourceDO existingMapping = groupResourceMapper.selectOne(
                    new LambdaQueryWrapper<GroupResourceDO>()
                            .eq(GroupResourceDO::getGroupId, groupId)
                            .eq(GroupResourceDO::getResourceId, resourceId)
            );
            
            if (existingMapping != null) {
                return ResultUtil.fail(ResourceErrorCode.FOLDER_ALREADY_EXISTS);
            }
            
            // 添加资源到组
            GroupResourceDO mapping = new GroupResourceDO();
            mapping.setGroupId(groupId);
            mapping.setResourceId(resourceId);
            mapping.setCreatedAt(new Date());
            
            groupResourceMapper.insert(mapping);
            
            return ResultUtil.success();
        } catch (Exception e) {
            log.error("添加资源到组失败", e);
            return ResultUtil.fail(ResourceErrorCode.OPERATION_FAILED);
        }
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<Void> removeResourceFromGroup(Long groupId, Long resourceId, Long userId) {
        try {
            // 验证用户是否属于该组
            if (!isUserInGroup(userId, groupId)) {
                return ResultUtil.fail(ResourceErrorCode.RESOURCE_ACCESS_DENIED);
            }
            
            // 删除组资源关系
            groupResourceMapper.delete(
                    new LambdaQueryWrapper<GroupResourceDO>()
                            .eq(GroupResourceDO::getGroupId, groupId)
                            .eq(GroupResourceDO::getResourceId, resourceId)
            );
            
            return ResultUtil.success();
        } catch (Exception e) {
            log.error("从组中移除资源失败", e);
            return ResultUtil.fail(ResourceErrorCode.OPERATION_FAILED);
        }
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<Void> inviteUsersToGroup(InviteUserToGroupRequestDTO requestDTO) {
        try {
            // 验证邀请者是否属于该组
            if (!isUserInGroup(requestDTO.getInviterUserId(), requestDTO.getGroupId())) {
                return ResultUtil.fail(ResourceErrorCode.RESOURCE_ACCESS_DENIED);
            }
            
            // 批量添加用户到组
            for (Long userId : requestDTO.getInvitedUserIds()) {
                // 检查用户是否已在组中
                if (!isUserInGroup(userId, requestDTO.getGroupId())) {
                    UserGroupMappingDO mapping = new UserGroupMappingDO();
                    mapping.setUserId(userId);
                    mapping.setGroupId(requestDTO.getGroupId());
                    mapping.setJoinedAt(new Date());
                    
                    userGroupMappingMapper.insert(mapping);
                }
            }
            
            return ResultUtil.success();
        } catch (Exception e) {
            log.error("邀请用户加入组失败", e);
            return ResultUtil.fail(ResourceErrorCode.OPERATION_FAILED);
        }
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<Void> removeUserFromGroup(Long groupId, Long userId, Long operatorId) {
        try {
            // 查询组信息
            UserGroupDO group = userGroupMapper.selectById(groupId);
            if (group == null) {
                return ResultUtil.fail(ResourceErrorCode.FOLDER_NOT_FOUND);
            }
            
            // 权限检查：只有组创建者或用户自己可以移除
            if (!Objects.equals(group.getCreateUser(), operatorId) && !Objects.equals(userId, operatorId)) {
                return ResultUtil.fail(ResourceErrorCode.RESOURCE_ACCESS_DENIED);
            }
            
            // 不能移除组创建者
            if (Objects.equals(group.getCreateUser(), userId)) {
                return ResultUtil.fail(ResourceErrorCode.INVALID_PARAMETER);
            }
            
            // 移除用户
            userGroupMappingMapper.delete(
                    new LambdaQueryWrapper<UserGroupMappingDO>()
                            .eq(UserGroupMappingDO::getGroupId, groupId)
                            .eq(UserGroupMappingDO::getUserId, userId)
            );
            
            return ResultUtil.success();
        } catch (Exception e) {
            log.error("从组中移除用户失败", e);
            return ResultUtil.fail(ResourceErrorCode.OPERATION_FAILED);
        }
    }
    
    /**
     * 转换为组项
     */
    private GroupListResponseDTO.GroupItem convertToGroupItem(UserGroupDO group, Long currentUserId) {
        GroupListResponseDTO.GroupItem item = new GroupListResponseDTO.GroupItem();
        BeanUtils.copyProperties(group, item);
        
        // 统计成员数量
        long memberCount = userGroupMappingMapper.selectCount(
                new LambdaQueryWrapper<UserGroupMappingDO>()
                        .eq(UserGroupMappingDO::getGroupId, group.getId())
        );
        item.setMemberCount((int) memberCount);
        
        // 判断是否为创建者
        item.setIsCreator(Objects.equals(group.getCreateUser(), currentUserId));
        
        return item;
    }
    
    /**
     * 转换为资源项
     */
    private ResourceListResponseDTO.ResourceItem convertToResourceItem(ResourceDO resource) {
        ResourceListResponseDTO.ResourceItem item = new ResourceListResponseDTO.ResourceItem();
        BeanUtils.copyProperties(resource, item);
        
        // 查询文件路径等详情信息
        ResourceDetailDO detail = resourceDetailMapper.selectOne(
                new LambdaQueryWrapper<ResourceDetailDO>()
                        .eq(ResourceDetailDO::getResourceId, resource.getId())
        );
        
        if (detail != null) {
            item.setFilePath(detail.getFilePath());
        }
        
        return item;
    }
    
    /**
     * 检查用户是否属于组
     */
    private boolean isUserInGroup(Long userId, Long groupId) {
        UserGroupMappingDO mapping = userGroupMappingMapper.selectOne(
                new LambdaQueryWrapper<UserGroupMappingDO>()
                        .eq(UserGroupMappingDO::getUserId, userId)
                        .eq(UserGroupMappingDO::getGroupId, groupId)
        );
        return mapping != null;
    }
} 