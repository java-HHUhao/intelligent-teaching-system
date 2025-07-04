package cn.edu.hhu.its.user.service.service;

import cn.edu.hhu.its.user.service.model.dto.response.UserPermissionRespDTO;

public interface AuthService {
    /**
     * 获取用户权限信息
     *
     * @param userId 用户ID
     * @param username 用户名
     * @return 用户权限信息
     */
    UserPermissionRespDTO getUserPermissions(Long userId, String username);
}
