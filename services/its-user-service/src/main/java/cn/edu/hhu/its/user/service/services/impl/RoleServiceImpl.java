package cn.edu.hhu.its.user.service.services.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.edu.hhu.its.user.service.model.domain.Role;
import cn.edu.hhu.its.user.service.services.RoleService;
import cn.edu.hhu.its.user.service.mapper.RoleMapper;
import org.springframework.stereotype.Service;

/**
* @author lianghao
* @description 针对表【role(系统角色表)】的数据库操作Service实现
* @createDate 2025-06-23 15:50:02
*/
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role>
    implements RoleService{

}




