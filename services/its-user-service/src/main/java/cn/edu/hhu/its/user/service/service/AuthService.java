package cn.edu.hhu.its.user.service.service;

import cn.edu.hhu.spring.boot.starter.common.dto.UserPermissionDTO;

public interface AuthService {
    /**
     * 获取用户权限信息
     *
     * @param userId 用户ID
     * @param username 用户名
     * @return 用户权限信息
     */
    UserPermissionDTO getUserPermissions(Long userId, String username);
}
