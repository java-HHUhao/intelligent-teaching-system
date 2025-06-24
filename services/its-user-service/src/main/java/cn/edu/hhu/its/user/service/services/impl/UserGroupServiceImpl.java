package cn.edu.hhu.its.user.service.services.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.edu.hhu.its.user.service.model.domain.UserGroup;
import cn.edu.hhu.its.user.service.services.UserGroupService;
import cn.edu.hhu.its.user.service.mapper.UserGroupMapper;
import org.springframework.stereotype.Service;

/**
* @author lianghao
* @description 针对表【user_group(用户组表，定义教学组/班级等逻辑分组)】的数据库操作Service实现
* @createDate 2025-06-23 15:50:12
*/
@Service
public class UserGroupServiceImpl extends ServiceImpl<UserGroupMapper, UserGroup>
    implements UserGroupService{

}




