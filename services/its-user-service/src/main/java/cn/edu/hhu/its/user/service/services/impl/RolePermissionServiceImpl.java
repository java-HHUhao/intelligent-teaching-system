package cn.edu.hhu.its.user.service.services.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.edu.hhu.its.user.service.model.domain.RolePermission;
import cn.edu.hhu.its.user.service.services.RolePermissionService;
import cn.edu.hhu.its.user.service.mapper.RolePermissionMapper;
import org.springframework.stereotype.Service;

/**
* @author lianghao
* @description 针对表【role_permission(角色权限关系表)】的数据库操作Service实现
* @createDate 2025-06-23 15:50:05
*/
@Service
public class RolePermissionServiceImpl extends ServiceImpl<RolePermissionMapper, RolePermission>
    implements RolePermissionService{

}




