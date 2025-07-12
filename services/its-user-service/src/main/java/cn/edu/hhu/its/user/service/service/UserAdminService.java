package cn.edu.hhu.its.user.service.service;

import cn.edu.hhu.its.user.service.model.dto.request.RoleCreateReqDTO;
import cn.edu.hhu.its.user.service.model.dto.request.RolePermissionAssignReqDTO;
import cn.edu.hhu.its.user.service.model.dto.request.UserListReqDTO;
import cn.edu.hhu.its.user.service.model.dto.request.PermissionCreateReqDTO;
import cn.edu.hhu.its.user.service.model.dto.request.PermissionUpdateReqDTO;
import cn.edu.hhu.its.user.service.model.dto.response.PermissionRespDTO;
import cn.edu.hhu.its.user.service.model.dto.response.RoleRespDTO;
import cn.edu.hhu.its.user.service.model.dto.response.UserListRespDTO;
import cn.edu.hhu.spring.boot.starter.common.exception.ClientException;
import cn.edu.hhu.spring.boot.starter.common.page.PageResult;

import java.util.List;

/**
 * 用户管理服务接口
 */
public interface UserAdminService {
    /**
     * 获取用户列表
     */
    PageResult<UserListRespDTO> getUserList();

    /**
     * 条件查询用户列表
     * @param queryReq 查询条件
     * @return 分页用户列表
     */
    PageResult<UserListRespDTO> searchUserList(UserListReqDTO queryReq);

    /**
     * 获取所有角色列表
     * @return 角色列表
     */
    List<RoleRespDTO> listRoles();

    /**
     * 创建角色
     * @param reqDTO 创建角色请求参数
     * @return 创建的角色ID
     */
    RoleRespDTO createRole(RoleCreateReqDTO reqDTO);

    /**
     * 为角色分配权限
     * @param reqDTO 权限分配请求参数
     */
    void assignRolePermissions(RolePermissionAssignReqDTO reqDTO);

    /**
     * 获取所有权限列表（树形结构）
     * @return 权限树
     */
    List<PermissionRespDTO> listPermissions();

    /**
     * 获取指定角色的权限列表（树形结构）
     *
     * @param roleName 角色名称
     * @return 权限列表，以树形结构返回
     */
    List<PermissionRespDTO> getRolePermissionsByName(String roleName);

    /**
     * 删除角色
     *
     * @param roleName 角色名称
     * @throws ClientException 如果角色不存在，抛出 ROLE_NOT_FOUND 异常
     */
    void deleteRoleByName(String roleName);

    /**
     * 删除权限
     *
     * @param permissionCode 权限code
     * @throws ClientException 如果权限不存在，抛出 PERMISSION_NOT_FOUND 异常
     * @throws ClientException 如果权限存在子权限，抛出 PERMISSION_HAS_CHILDREN 异常
     */
    void deletePermissionByCode(String permissionCode);

    /**
     * 创建权限
     *
     * @param createReq 创建权限请求
     * @return 创建的权限信息
     * @throws ClientException 如果权限编码已存在，抛出 PERMISSION_CODE_EXIST 异常
     * @throws ClientException 如果父权限不存在，抛出 PERMISSION_NOT_FOUND 异常
     */
    PermissionRespDTO createPermission(PermissionCreateReqDTO createReq);

    /**
     * 更新权限信息
     *
     * @param updateReq 更新权限请求
     * @return 更新后的权限信息
     * @throws ClientException 如果权限不存在，抛出 PERMISSION_NOT_FOUND 异常
     * @throws ClientException 如果权限编码已存在，抛出 PERMISSION_CODE_EXIST 异常
     * @throws ClientException 如果父权限设置无效，抛出 PERMISSION_PARENT_ERROR 异常
     */
    PermissionRespDTO updatePermission(PermissionUpdateReqDTO updateReq);
}
