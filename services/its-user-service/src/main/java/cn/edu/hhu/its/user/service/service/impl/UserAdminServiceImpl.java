package cn.edu.hhu.its.user.service.service.impl;

import cn.edu.hhu.its.user.service.common.enums.UserErrorCode;
import cn.edu.hhu.its.user.service.model.domain.RoleDO;
import cn.edu.hhu.its.user.service.model.domain.UserDO;
import cn.edu.hhu.its.user.service.model.domain.UserDetailDO;
import cn.edu.hhu.its.user.service.model.domain.RolePermissionDO;
import cn.edu.hhu.its.user.service.model.domain.PermissionDO;
import cn.edu.hhu.its.user.service.model.domain.UserRoleDO;
import cn.edu.hhu.its.user.service.model.dto.request.RoleCreateReqDTO;
import cn.edu.hhu.its.user.service.model.dto.request.RoleUpdateReqDTO;
import cn.edu.hhu.its.user.service.model.dto.request.UserListReqDTO;
import cn.edu.hhu.its.user.service.model.dto.request.UserStatusUpdateReqDTO;
import cn.edu.hhu.its.user.service.model.dto.request.RolePermissionAssignReqDTO;
import cn.edu.hhu.its.user.service.model.dto.request.PermissionCreateReqDTO;
import cn.edu.hhu.its.user.service.model.dto.request.PermissionUpdateReqDTO;
import cn.edu.hhu.its.user.service.model.dto.response.RoleRespDTO;
import cn.edu.hhu.its.user.service.model.dto.response.UserListRespDTO;
import cn.edu.hhu.its.user.service.model.dto.response.PermissionRespDTO;
import cn.edu.hhu.its.user.service.model.mapper.RoleMapper;
import cn.edu.hhu.its.user.service.model.mapper.UserDetailMapper;
import cn.edu.hhu.its.user.service.model.mapper.UserMapper;
import cn.edu.hhu.its.user.service.model.mapper.RolePermissionMapper;
import cn.edu.hhu.its.user.service.model.mapper.PermissionMapper;
import cn.edu.hhu.its.user.service.model.mapper.UserRoleMapper;
import cn.edu.hhu.its.user.service.service.UserAdminService;
import cn.edu.hhu.spring.boot.starter.common.exception.ClientException;
import cn.edu.hhu.spring.boot.starter.common.page.PageResult;
import cn.edu.hhu.spring.boot.starter.common.utils.ExceptionUtil;
import cn.edu.hhu.spring.boot.starter.distributedid.handler.IdGeneratorManager;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.HashMap;

import static cn.edu.hhu.its.user.service.common.contant.UserConstant.USER_SERVICE_ID;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserAdminServiceImpl implements UserAdminService {
    private final UserMapper userMapper;
    private final UserDetailMapper userDetailMapper;
    private final RoleMapper roleMapper;
    private final PermissionMapper permissionMapper;
    private final RolePermissionMapper rolePermissionMapper;
    private final UserRoleMapper userRoleMapper;

    @Override
    public PageResult<UserListRespDTO> getUserList() {
        // 查询所有用户（排除已删除的用户）
        List<UserDO> userList = userMapper.selectList(
                new LambdaQueryWrapper<UserDO>()
                        .eq(UserDO::getIsDeleted, false)
                        .orderByDesc(UserDO::getCreatedAt)
        );
        
        if (userList.isEmpty()) {
            return PageResult.<UserListRespDTO>builder()
                    .pageNum(1)
                    .pageSize(0)
                    .total(0L)
                    .list(List.of())
                    .build();
        }

        // 获取用户ID列表
        List<Long> userIds = userList.stream()
                .map(UserDO::getId)
                .collect(Collectors.toList());

        // 批量查询用户详情
        List<UserDetailDO> userDetails = userDetailMapper.selectList(
                new LambdaQueryWrapper<UserDetailDO>()
                        .in(UserDetailDO::getUserId, userIds)
        );

        // 构建用户ID到详情的映射
        Map<Long, UserDetailDO> userDetailMap = userDetails.stream()
                .collect(Collectors.toMap(UserDetailDO::getUserId, detail -> detail));
        
        // 转换为DTO（手动设置字段以确保正确映射）
        List<UserListRespDTO> userListRespDTOs = userList.stream()
                .map(user -> {
                    UserDetailDO detail = userDetailMap.get(user.getId());
                    return new UserListRespDTO()
                            .setUserId(user.getId())  // 手动设置userId
                            .setUsername(user.getUsername())
                            .setEmail(user.getEmail())
                            .setPhone(user.getPhone())
                            .setStatus(user.getStatus())
                            .setCreatedAt(user.getCreatedAt())
                            .setUpdatedAt(user.getUpdatedAt())
                            .setAvatarUrl(detail != null ? detail.getAvatarUrl() : null);
                })
                .collect(Collectors.toList());
        
        // 构建分页结果
        PageResult<UserListRespDTO> pageResult = new PageResult<>();
        pageResult.setList(userListRespDTOs);
        pageResult.setTotal((long) userListRespDTOs.size());
        pageResult.setPageSize(userListRespDTOs.size());
        pageResult.setPageNum(1);
        
        return pageResult;
    }

    @Override
    public PageResult<UserListRespDTO> searchUserList(UserListReqDTO queryReq) {
        // 构建查询条件
        LambdaQueryWrapper<UserDO> queryWrapper = new LambdaQueryWrapper<UserDO>()
                .like(StringUtils.isNotBlank(queryReq.getUsername()), UserDO::getUsername, queryReq.getUsername())
                .like(StringUtils.isNotBlank(queryReq.getEmail()), UserDO::getEmail, queryReq.getEmail())
                .like(StringUtils.isNotBlank(queryReq.getPhone()), UserDO::getPhone, queryReq.getPhone())
                .eq(queryReq.getStatus() != null, UserDO::getStatus, queryReq.getStatus())
                .eq(UserDO::getIsDeleted, false)
                .orderByDesc(UserDO::getCreatedAt);

        // 执行分页查询
        Page<UserDO> page = userMapper.selectPage(
                new Page<>(queryReq.getPageNum(), queryReq.getPageSize()),
                queryWrapper
        );

        // 如果没有数据，返回空结果
        if (page.getRecords().isEmpty()) {
            return PageResult.<UserListRespDTO>builder()
                    .pageNum(queryReq.getPageNum())
                    .pageSize(queryReq.getPageSize())
                    .total(0L)
                    .list(List.of())
                    .build();
        }

        // 获取用户ID列表
        List<Long> userIds = page.getRecords().stream()
                .map(UserDO::getId)
                .collect(Collectors.toList());

        // 批量查询用户详情
        List<UserDetailDO> userDetails = userDetailMapper.selectList(
                new LambdaQueryWrapper<UserDetailDO>()
                        .in(UserDetailDO::getUserId, userIds)
        );

        // 构建用户ID到详情的映射
        Map<Long, UserDetailDO> userDetailMap = userDetails.stream()
                .collect(Collectors.toMap(UserDetailDO::getUserId, detail -> detail));

        // 转换为响应对象
        List<UserListRespDTO> userList = page.getRecords().stream()
                .map(user -> {
                    UserDetailDO detail = userDetailMap.get(user.getId());
                    return new UserListRespDTO()
                            .setUserId(user.getId())
                            .setUsername(user.getUsername())
                            .setEmail(user.getEmail())
                            .setPhone(user.getPhone())
                            .setStatus(user.getStatus())
                            .setCreatedAt(user.getCreatedAt())
                            .setUpdatedAt(user.getUpdatedAt())
                            .setAvatarUrl(detail != null ? detail.getAvatarUrl() : null);
                })
                .collect(Collectors.toList());

        // 构建分页结果
        return PageResult.<UserListRespDTO>builder()
                .pageNum(queryReq.getPageNum())
                .pageSize(queryReq.getPageSize())
                .total(page.getTotal())
                .list(userList)
                .build();
    }

    @Override
    public List<RoleRespDTO> listRoles() {
        // 1. 查询所有角色，按创建时间降序排序
        List<RoleDO> roles = roleMapper.selectList(
                new LambdaQueryWrapper<RoleDO>()
                        .orderByDesc(RoleDO::getCreatedAt)
        );

        if (roles.isEmpty()) {
            return List.of();
        }

        // 2. 获取角色ID列表
        List<Long> roleIds = roles.stream()
                .map(RoleDO::getId)
                .collect(Collectors.toList());

        // 3. 统计每个角色的用户数量（使用单条SQL查询）
        Map<Long, Long> roleUserCountMap = userRoleMapper.selectList(
                new LambdaQueryWrapper<UserRoleDO>()
                        .in(UserRoleDO::getRoleId, roleIds)
        ).stream().collect(
                Collectors.groupingBy(
                        UserRoleDO::getRoleId,
                        Collectors.counting()
                )
        );

        // 4. 转换为响应对象，并设置用户数量
        return roles.stream()
                .map(role -> new RoleRespDTO()
                        .setId(role.getId())
                        .setName(role.getName())
                        .setDescription(role.getDescription())
                        .setUserCount(roleUserCountMap.getOrDefault(role.getId(), 0L))
                        .setCreatedAt(role.getCreatedAt())
                        .setUpdatedAt(role.getUpdatedAt()))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public RoleRespDTO createRole(RoleCreateReqDTO createReq) {
        // 检查角色名是否已存在
        Long count = roleMapper.selectCount(
                new LambdaQueryWrapper<RoleDO>()
                        .eq(RoleDO::getName, createReq.getName())
        );
        ExceptionUtil.throwIf(count > 0,
                () -> new ClientException(UserErrorCode.ROLE_NAME_EXIST_ERROR));

        // 创建角色实体
        RoleDO role = new RoleDO()
                .setName(createReq.getName())
                .setDescription(createReq.getDescription())
                .setCreatedAt(new Date())
                .setUpdatedAt(new Date());

        // 保存到数据库
        roleMapper.insert(role);

        // 返回创建的角色信息
        return new RoleRespDTO()
                .setId(role.getId())
                .setName(role.getName())
                .setDescription(role.getDescription())
                .setCreatedAt(role.getCreatedAt())
                .setUpdatedAt(role.getUpdatedAt());
    }

    @Override
    @Transactional
    public void assignRolePermissions(RolePermissionAssignReqDTO reqDTO) {
        // 1. 通过角色名称查找角色
        RoleDO role = roleMapper.selectOne(
            new LambdaQueryWrapper<RoleDO>()
                .eq(RoleDO::getName, reqDTO.getRoleName())
        );
        ExceptionUtil.throwIf(role == null,
                () -> new ClientException(UserErrorCode.ROLE_NOT_FOUND));

        // 2. 通过权限代码查找权限
        List<PermissionDO> permissions = permissionMapper.selectList(
                new LambdaQueryWrapper<PermissionDO>()
                        .in(PermissionDO::getCode, reqDTO.getPermissionCodes())
        );
        ExceptionUtil.throwIf(permissions.size() != reqDTO.getPermissionCodes().size(),
                () -> new ClientException(UserErrorCode.PERMISSION_NOT_FOUND));

        // 3. 删除原有的角色-权限关系
        rolePermissionMapper.delete(
                new LambdaQueryWrapper<RolePermissionDO>()
                        .eq(RolePermissionDO::getRoleId, role.getId())
        );

        // 4. 批量插入新的角色-权限关系
        List<RolePermissionDO> rolePermissions = permissions.stream()
                .map(permission -> {
                    RolePermissionDO rolePermission = new RolePermissionDO();
                    rolePermission.setRoleId(role.getId());
                    rolePermission.setPermissionId(permission.getId());
                    rolePermission.setCreatedAt(new Date());
                    return rolePermission;
                })
                .toList();

        // 批量插入
        rolePermissions.forEach(rolePermissionMapper::insert);

        log.info("Role {} permissions assigned successfully", reqDTO.getRoleName());
    }

    @Override
    public List<PermissionRespDTO> listPermissions() {
        // 1. 查询所有权限
        List<PermissionDO> permissions = permissionMapper.selectList(
                new LambdaQueryWrapper<PermissionDO>()
                        .orderByAsc(PermissionDO::getParentId)
                        .orderByAsc(PermissionDO::getCreatedAt)
        );

        // 2. 转换为DTO
        Map<Long, PermissionRespDTO> permissionMap = permissions.stream()
                .map(permission -> new PermissionRespDTO()
                        .setId(permission.getId())
                        .setCode(permission.getCode())
                        .setName(permission.getName())
                        .setType(permission.getType())
                        .setParentId(permission.getParentId())
                        .setDescription(permission.getDescription())
                        .setCreatedAt(permission.getCreatedAt())
                        .setUpdatedAt(permission.getUpdatedAt())
                        .setChildren(new ArrayList<>()))
                .collect(Collectors.toMap(PermissionRespDTO::getId, p -> p));

        // 3. 构建树形结构
        List<PermissionRespDTO> rootPermissions = new ArrayList<>();
        permissionMap.values().forEach(permission -> {
            if (permission.getParentId() == null || permission.getParentId() == 0) {
                rootPermissions.add(permission);
            } else {
                PermissionRespDTO parent = permissionMap.get(permission.getParentId());
                if (parent != null) {
                    parent.getChildren().add(permission);
                }
            }
        });

        return rootPermissions;
    }

    @Override
    public List<PermissionRespDTO> getRolePermissionsByName(String roleName) {
        // 1. 验证角色是否存在
        RoleDO role = roleMapper.selectOne(
            new LambdaQueryWrapper<RoleDO>()
                .eq(RoleDO::getName, roleName)
        );
        ExceptionUtil.throwIf(role == null,
                () -> new ClientException(UserErrorCode.ROLE_NOT_FOUND));

        // 2. 查询角色的权限关系
        List<RolePermissionDO> rolePermissions = rolePermissionMapper.selectList(
                new LambdaQueryWrapper<RolePermissionDO>()
                        .eq(RolePermissionDO::getRoleId, role.getId())
        );

        if (rolePermissions.isEmpty()) {
            return List.of();
        }

        // 3. 获取权限ID列表
        List<Long> permissionIds = rolePermissions.stream()
                .map(RolePermissionDO::getPermissionId)
                .collect(Collectors.toList());

        // 4. 查询权限详情
        List<PermissionDO> permissions = permissionMapper.selectList(
                new LambdaQueryWrapper<PermissionDO>()
                        .in(PermissionDO::getId, permissionIds)
                        .orderByAsc(PermissionDO::getParentId)
                        .orderByAsc(PermissionDO::getCreatedAt)
        );

        // 5. 转换为DTO并构建树形结构
        Map<Long, PermissionRespDTO> permissionMap = permissions.stream()
                .map(permission -> new PermissionRespDTO()
                        .setId(permission.getId())
                        .setCode(permission.getCode())
                        .setName(permission.getName())
                        .setType(permission.getType())
                        .setParentId(permission.getParentId())
                        .setDescription(permission.getDescription())
                        .setCreatedAt(permission.getCreatedAt())
                        .setUpdatedAt(permission.getUpdatedAt())
                        .setChildren(new ArrayList<>()))
                .collect(Collectors.toMap(PermissionRespDTO::getId, p -> p));

        // 6. 构建树形结构
        List<PermissionRespDTO> rootPermissions = new ArrayList<>();
        permissionMap.values().forEach(permission -> {
            if (permission.getParentId() == null || permission.getParentId() == 0) {
                rootPermissions.add(permission);
            } else {
                PermissionRespDTO parent = permissionMap.get(permission.getParentId());
                if (parent != null) {
                    parent.getChildren().add(permission);
                }
            }
        });

        return rootPermissions;
    }

    @Override
    @Transactional
    public void deleteRoleByName(String roleName) {
        // 1. 验证角色是否存在
        RoleDO role = roleMapper.selectOne(
            new LambdaQueryWrapper<RoleDO>()
                .eq(RoleDO::getName, roleName)
        );
        ExceptionUtil.throwIf(role == null,
                () -> new ClientException(UserErrorCode.ROLE_NOT_FOUND));

        // 2. 删除角色-权限关联关系
        rolePermissionMapper.delete(
                new LambdaQueryWrapper<RolePermissionDO>()
                        .eq(RolePermissionDO::getRoleId, role.getId())
        );

        // 3. 删除角色
        roleMapper.deleteById(role.getId());

        log.info("Role {} deleted successfully", roleName);
    }

    @Override
    @Transactional
    public void deletePermissionByCode(String permissionCode) {
        // 1. 验证权限是否存在
        PermissionDO permission = permissionMapper.selectOne(
            new LambdaQueryWrapper<PermissionDO>()
                .eq(PermissionDO::getCode, permissionCode)
        );
        ExceptionUtil.throwIf(permission == null,
                () -> new ClientException(UserErrorCode.PERMISSION_NOT_FOUND));

        // 2. 检查是否有子权限
        Long childCount = permissionMapper.selectCount(
                new LambdaQueryWrapper<PermissionDO>()
                        .eq(PermissionDO::getParentId, permission.getId())
        );
        ExceptionUtil.throwIf(childCount > 0,
                () -> new ClientException(UserErrorCode.PERMISSION_HAS_CHILDREN));

        // 3. 删除角色-权限关联关系
        rolePermissionMapper.delete(
                new LambdaQueryWrapper<RolePermissionDO>()
                        .eq(RolePermissionDO::getPermissionId, permission.getId())
        );

        // 4. 删除权限
        permissionMapper.deleteById(permission.getId());

        log.info("Permission {} deleted successfully", permissionCode);
    }

    @Override
    @Transactional
    public PermissionRespDTO createPermission(PermissionCreateReqDTO createReq) {
        // 1. 检查权限编码是否已存在
        Long count = permissionMapper.selectCount(
                new LambdaQueryWrapper<PermissionDO>()
                        .eq(PermissionDO::getCode, createReq.getCode())
        );
        ExceptionUtil.throwIf(count > 0,
                () -> new ClientException(UserErrorCode.PERMISSION_CODE_EXIST));

        // 2. 如果有父权限ID，验证父权限是否存在
        if (createReq.getParentId() != null) {
            PermissionDO parentPermission = permissionMapper.selectById(createReq.getParentId());
            ExceptionUtil.throwIf(parentPermission == null,
                    () -> new ClientException(UserErrorCode.PERMISSION_NOT_FOUND));
        }

        // 3. 创建权限实体
        PermissionDO permission = new PermissionDO()
                .setCode(createReq.getCode())
                .setName(createReq.getName())
                .setType(createReq.getType())
                .setParentId(createReq.getParentId())
                .setDescription(createReq.getDescription())
                .setCreatedAt(new Date())
                .setUpdatedAt(new Date());

        // 4. 保存到数据库
        permissionMapper.insert(permission);

        // 5. 返回创建的权限信息
        return new PermissionRespDTO()
                .setId(permission.getId())
                .setCode(permission.getCode())
                .setName(permission.getName())
                .setType(permission.getType())
                .setParentId(permission.getParentId())
                .setDescription(permission.getDescription())
                .setCreatedAt(permission.getCreatedAt())
                .setUpdatedAt(permission.getUpdatedAt())
                .setChildren(new ArrayList<>());
    }

    @Override
    @Transactional
    public PermissionRespDTO updatePermission(PermissionUpdateReqDTO updateReq) {
        // 1. 验证权限是否存在
        PermissionDO permission = permissionMapper.selectById(updateReq.getId());
        ExceptionUtil.throwIf(permission == null,
                () -> new ClientException(UserErrorCode.PERMISSION_NOT_FOUND));

        // 2. 检查权限编码是否与其他权限重复
        Long count = permissionMapper.selectCount(
                new LambdaQueryWrapper<PermissionDO>()
                        .eq(PermissionDO::getCode, updateReq.getCode())
                        .ne(PermissionDO::getId, updateReq.getId())
        );
        ExceptionUtil.throwIf(count > 0,
                () -> new ClientException(UserErrorCode.PERMISSION_CODE_EXIST));

        // 3. 如果有父权限ID，验证父权限是否存在且不是自己或自己的子权限
        if (updateReq.getParentId() != null) {
            // 3.1 不能将自己设为父权限
            ExceptionUtil.throwIf(updateReq.getParentId().equals(updateReq.getId()),
                    () -> new ClientException(UserErrorCode.PERMISSION_PARENT_ERROR));

            // 3.2 验证父权限是否存在
            PermissionDO parentPermission = permissionMapper.selectById(updateReq.getParentId());
            ExceptionUtil.throwIf(parentPermission == null,
                    () -> new ClientException(UserErrorCode.PERMISSION_NOT_FOUND));

            // 3.3 检查是否将权限设置为其子权限的子权限
            List<PermissionDO> children = permissionMapper.selectList(
                    new LambdaQueryWrapper<PermissionDO>()
                            .eq(PermissionDO::getParentId, updateReq.getId())
            );
            if (!children.isEmpty()) {
                List<Long> childrenIds = new ArrayList<>();
                getChildrenIds(updateReq.getId(), childrenIds);
                ExceptionUtil.throwIf(childrenIds.contains(updateReq.getParentId()),
                        () -> new ClientException(UserErrorCode.PERMISSION_PARENT_ERROR));
            }
        }

        // 4. 更新权限信息
        permission.setCode(updateReq.getCode())
                .setName(updateReq.getName())
                .setType(updateReq.getType())
                .setParentId(updateReq.getParentId())
                .setDescription(updateReq.getDescription())
                .setUpdatedAt(new Date());

        permissionMapper.updateById(permission);

        // 5. 返回更新后的权限信息
        return new PermissionRespDTO()
                .setId(permission.getId())
                .setCode(permission.getCode())
                .setName(permission.getName())
                .setType(permission.getType())
                .setParentId(permission.getParentId())
                .setDescription(permission.getDescription())
                .setCreatedAt(permission.getCreatedAt())
                .setUpdatedAt(permission.getUpdatedAt())
                .setChildren(new ArrayList<>());
    }

    @Override
    @Transactional
    public RoleRespDTO updateRole(RoleUpdateReqDTO updateReq) {
        // 1. 验证角色是否存在
        RoleDO role = roleMapper.selectById(updateReq.getId());
        ExceptionUtil.throwIf(role == null,
                () -> new ClientException(UserErrorCode.ROLE_NOT_FOUND));

        // 2. 检查角色名称是否与其他角色重复
        Long count = roleMapper.selectCount(
                new LambdaQueryWrapper<RoleDO>()
                        .eq(RoleDO::getName, updateReq.getName())
                        .ne(RoleDO::getId, updateReq.getId())
        );
        ExceptionUtil.throwIf(count > 0,
                () -> new ClientException(UserErrorCode.ROLE_NAME_EXIST_ERROR));

        // 3. 更新角色信息
        RoleDO updateRole = new RoleDO()
                .setId(updateReq.getId())
                .setName(updateReq.getName())
                .setDescription(updateReq.getDescription())
                .setUpdatedAt(new Date());

        roleMapper.updateById(updateRole);

        // 4. 返回更新后的角色信息
        return new RoleRespDTO()
                .setId(updateRole.getId())
                .setName(updateRole.getName())
                .setDescription(updateRole.getDescription())
                .setCreatedAt(role.getCreatedAt())
                .setUpdatedAt(updateRole.getUpdatedAt());
    }

    @Override
    @Transactional
    public void updateUserStatus(UserStatusUpdateReqDTO updateReq) {
        // 1. 检查用户是否存在
        UserDO user = userMapper.selectById(updateReq.getUserId());
        ExceptionUtil.throwIf(user == null || Boolean.TRUE.equals(user.getIsDeleted()),
                () -> new ClientException(UserErrorCode.USER_NOT_EXIST));

        // 2. 更新用户状态
        user.setStatus(updateReq.getStatus())
            .setUpdatedAt(new Date());
        
        userMapper.updateById(user);
    }

    /**
     * 递归获取所有子权限ID
     */
    private void getChildrenIds(Long permissionId, List<Long> childrenIds) {
        List<PermissionDO> children = permissionMapper.selectList(
                new LambdaQueryWrapper<PermissionDO>()
                        .eq(PermissionDO::getParentId, permissionId)
        );
        if (!children.isEmpty()) {
            children.forEach(child -> {
                childrenIds.add(child.getId());
                getChildrenIds(child.getId(), childrenIds);
            });
        }
    }
}
