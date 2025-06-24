package cn.edu.hhu.its.user.service.services.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.edu.hhu.its.user.service.model.domain.UserRole;
import cn.edu.hhu.its.user.service.services.UserRoleService;
import cn.edu.hhu.its.user.service.mapper.UserRoleMapper;
import org.springframework.stereotype.Service;

/**
* @author lianghao
* @description 针对表【user_role(用户角色关系表)】的数据库操作Service实现
* @createDate 2025-06-23 15:50:17
*/
@Service
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRole>
    implements UserRoleService{

}




