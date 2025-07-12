package cn.edu.hhu.its.user.service.controller;

import cn.edu.hhu.its.user.service.model.dto.request.RoleCreateReqDTO;
import cn.edu.hhu.its.user.service.model.dto.request.RolePermissionAssignReqDTO;
import cn.edu.hhu.its.user.service.model.dto.request.UserListReqDTO;
import cn.edu.hhu.its.user.service.model.dto.request.PermissionCreateReqDTO;
import cn.edu.hhu.its.user.service.model.dto.request.PermissionUpdateReqDTO;
import cn.edu.hhu.its.user.service.model.dto.response.RoleRespDTO;
import cn.edu.hhu.its.user.service.model.dto.response.UserListRespDTO;
import cn.edu.hhu.its.user.service.model.dto.response.PermissionRespDTO;
import cn.edu.hhu.its.user.service.service.UserAdminService;
import cn.edu.hhu.spring.boot.starter.common.page.PageResult;
import cn.edu.hhu.spring.boot.starter.common.result.Result;
import cn.edu.hhu.spring.boot.starter.common.utils.ResultUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user/admin")
@RequiredArgsConstructor
@RefreshScope
public class UserAdminController {
    private final UserAdminService userAdminService;

    /**
     * 获取用户列表
     */
    @GetMapping("/list")
    public Result<PageResult<UserListRespDTO>> getUserList() {
        return ResultUtil.success(userAdminService.getUserList());
    }

    /**
     * 条件查询用户列表
     */
    @PostMapping("/search")
    public Result<PageResult<UserListRespDTO>> searchUserList(@RequestBody UserListReqDTO queryReq) {
        return ResultUtil.success(userAdminService.searchUserList(queryReq));
    }

    /**
     * 获取所有角色列表
     */
    @GetMapping("/roles")
    public Result<List<RoleRespDTO>> getAllRoles() {
        return ResultUtil.success(userAdminService.listRoles());
    }

    /**
     * 创建新角色
     */
    @PostMapping("/roles")
    public Result<RoleRespDTO> createRole(@Validated @RequestBody RoleCreateReqDTO createReq) {
        return ResultUtil.success(userAdminService.createRole(createReq));
    }

    /**
     * 为角色分配权限
     */
    @PostMapping("/roles/permissions")
    public Result<Void> assignRolePermissions(@RequestBody @Valid RolePermissionAssignReqDTO reqDTO) {
        userAdminService.assignRolePermissions(reqDTO);
        return ResultUtil.success(null);
    }

    /**
     * 删除角色
     */
    @DeleteMapping("/roles/{roleName}")
    public Result<Void> deleteRole(@PathVariable("roleName") String roleName) {
        userAdminService.deleteRoleByName(roleName);
        return ResultUtil.success(null);
    }

    /**
     * 获取指定角色的权限列表（树形结构）
     */
    @GetMapping("/roles/{roleName}/permissions")
    public Result<List<PermissionRespDTO>> getRolePermissions(@PathVariable("roleName") String roleName) {
        return ResultUtil.success(userAdminService.getRolePermissionsByName(roleName));
    }

    /**
     * 获取所有权限列表（树形结构）
     */
    @GetMapping("/permissions")
    public Result<List<PermissionRespDTO>> getAllPermissions() {
        return ResultUtil.success(userAdminService.listPermissions());
    }

    /**
     * 创建权限
     */
    @PostMapping("/permissions")
    public Result<PermissionRespDTO> createPermission(@Validated @RequestBody PermissionCreateReqDTO createReq) {
        return ResultUtil.success(userAdminService.createPermission(createReq));
    }

    /**
     * 更新权限
     */
    @PutMapping("/permissions")
    public Result<PermissionRespDTO> updatePermission(@Validated @RequestBody PermissionUpdateReqDTO updateReq) {
        return ResultUtil.success(userAdminService.updatePermission(updateReq));
    }

    /**
     * 删除权限
     */
    @DeleteMapping("/permissions/{permissionCode}")
    public Result<Void> deletePermission(@PathVariable("permissionCode") String permissionCode) {
        userAdminService.deletePermissionByCode(permissionCode);
        return ResultUtil.success(null);
    }
}
