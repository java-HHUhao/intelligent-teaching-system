package cn.edu.hhu.its.user.service.service.impl;

import cn.edu.hhu.its.user.service.common.cache.UserCacheExpire;
import cn.edu.hhu.its.user.service.common.cache.UserCachePrefix;
import cn.edu.hhu.its.user.service.model.domain.PermissionDO;
import cn.edu.hhu.its.user.service.model.domain.RolePermissionDO;
import cn.edu.hhu.its.user.service.model.domain.UserRoleDO;
import cn.edu.hhu.its.user.service.model.dto.response.UserPermissionRespDTO;
import cn.edu.hhu.its.user.service.model.mapper.PermissionMapper;
import cn.edu.hhu.its.user.service.model.mapper.RolePermissionMapper;
import cn.edu.hhu.its.user.service.model.mapper.UserRoleMapper;
import cn.edu.hhu.its.user.service.service.AuthService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRoleMapper userRoleMapper;
    private final RolePermissionMapper rolePermissionMapper;
    private final PermissionMapper permissionMapper;
    private final RedisTemplate<String, Object> redisTemplate;


    @Override
    public UserPermissionRespDTO getUserPermissions(Long userId, String username) {
        // 1. 尝试从缓存中获取
        String cacheKey = UserCachePrefix.PERMISSIONS + userId;
        UserPermissionRespDTO cachedPermissions = (UserPermissionRespDTO) redisTemplate.opsForValue().get(cacheKey);
        if (cachedPermissions != null) {
            return cachedPermissions;
        }

        // 2. 缓存中没有，从数据库查询
        // 2.1 查询用户的角色
        List<UserRoleDO> userRoles = userRoleMapper.selectList(
                new LambdaQueryWrapper<UserRoleDO>()
                        .eq(UserRoleDO::getUserId, userId)
        );

        if (userRoles.isEmpty()) {
            // 用户没有角色，返回空权限列表
            UserPermissionRespDTO emptyPermissions = new UserPermissionRespDTO()
                    .setUserId(userId)
                    .setUsername(username)
                    .setPermissions(new ArrayList<>());
            // 将空结果也缓存，避免缓存穿透
            redisTemplate.opsForValue().set(cacheKey, emptyPermissions, UserCacheExpire.USER_PERMISSION_EXPIRE, TimeUnit.SECONDS);
            return emptyPermissions;
        }

        // 2.2 获取角色ID列表
        List<Long> roleIds = userRoles.stream()
                .map(UserRoleDO::getRoleId)
                .collect(Collectors.toList());

        // 2.3 查询角色对应的权限关系
        List<RolePermissionDO> rolePermissions = rolePermissionMapper.selectList(
                new LambdaQueryWrapper<RolePermissionDO>()
                        .in(RolePermissionDO::getRoleId, roleIds)
        );

        if (rolePermissions.isEmpty()) {
            // 角色没有权限，返回空权限列表
            UserPermissionRespDTO emptyPermissions = new UserPermissionRespDTO()
                    .setUserId(userId)
                    .setUsername(username)
                    .setPermissions(new ArrayList<>());
            redisTemplate.opsForValue().set(cacheKey, emptyPermissions, UserCacheExpire.USER_PERMISSION_EXPIRE, TimeUnit.SECONDS);
            return emptyPermissions;
        }

        // 2.4 获取权限ID列表
        List<Long> permissionIds = rolePermissions.stream()
                .map(RolePermissionDO::getPermissionId)
                .collect(Collectors.toList());

        // 2.5 查询权限详情
        List<PermissionDO> permissions = permissionMapper.selectList(
                new LambdaQueryWrapper<PermissionDO>()
                        .in(PermissionDO::getId, permissionIds)
        );

        // 2.6 提取权限编码
        List<String> permissionCodes = permissions.stream()
                .map(PermissionDO::getCode)
                .collect(Collectors.toList());

        // 3. 构建返回结果
        UserPermissionRespDTO result = new UserPermissionRespDTO()
                .setUserId(userId)
                .setUsername(username)
                .setPermissions(permissionCodes);

        // 4. 写入缓存
        redisTemplate.opsForValue().set(cacheKey, result, UserCacheExpire.USER_PERMISSION_EXPIRE, TimeUnit.SECONDS);

        return result;
    }
}
