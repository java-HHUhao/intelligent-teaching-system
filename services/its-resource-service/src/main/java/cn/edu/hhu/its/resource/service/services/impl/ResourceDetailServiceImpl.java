package cn.edu.hhu.its.resource.service.services.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.edu.hhu.its.resource.service.model.domain.ResourceDetail;
import cn.edu.hhu.its.resource.service.services.ResourceDetailService;
import cn.edu.hhu.its.resource.service.mapper.ResourceDetailMapper;
import org.springframework.stereotype.Service;

/**
* @author lianghao
* @description 针对表【resource_detail(资源详情表，记录资源描述与扩展信息)】的数据库操作Service实现
* @createDate 2025-06-23 15:46:08
*/
@Service
public class ResourceDetailServiceImpl extends ServiceImpl<ResourceDetailMapper, ResourceDetail>
    implements ResourceDetailService{

}




