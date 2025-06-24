package cn.edu.hhu.its.user.service.services.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.edu.hhu.its.user.service.model.domain.UserGroupMapping;
import cn.edu.hhu.its.user.service.services.UserGroupMappingService;
import cn.edu.hhu.its.user.service.mapper.UserGroupMappingMapper;
import org.springframework.stereotype.Service;

/**
* @author lianghao
* @description 针对表【user_group_mapping(用户与用户组的多对多关联表)】的数据库操作Service实现
* @createDate 2025-06-23 15:50:14
*/
@Service
public class UserGroupMappingServiceImpl extends ServiceImpl<UserGroupMappingMapper, UserGroupMapping>
    implements UserGroupMappingService{

}




