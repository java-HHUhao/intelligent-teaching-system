package cn.edu.hhu.its.resource.service.services.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.edu.hhu.its.resource.service.model.domain.GroupResource;
import cn.edu.hhu.its.resource.service.services.GroupResourceService;
import cn.edu.hhu.its.resource.service.mapper.GroupResourceMapper;
import org.springframework.stereotype.Service;

/**
* @author lianghao
* @description 针对表【group_resource(用户组与资源的关联表，支持组共享资源)】的数据库操作Service实现
* @createDate 2025-06-23 15:46:02
*/
@Service
public class GroupResourceServiceImpl extends ServiceImpl<GroupResourceMapper, GroupResource>
    implements GroupResourceService{

}




