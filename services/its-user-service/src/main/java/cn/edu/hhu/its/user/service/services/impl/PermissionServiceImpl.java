package cn.edu.hhu.its.user.service.services.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.edu.hhu.its.user.service.model.domain.Permission;
import cn.edu.hhu.its.user.service.services.PermissionService;
import cn.edu.hhu.its.user.service.mapper.PermissionMapper;
import org.springframework.stereotype.Service;

/**
* @author lianghao
* @description 针对表【permission(系统权限表)】的数据库操作Service实现
* @createDate 2025-06-23 15:49:59
*/
@Service
public class PermissionServiceImpl extends ServiceImpl<PermissionMapper, Permission>
    implements PermissionService{

}




