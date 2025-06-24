package cn.edu.hhu.its.resource.service.services.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.edu.hhu.its.resource.service.model.domain.Resource;
import cn.edu.hhu.its.resource.service.services.ResourceService;
import cn.edu.hhu.its.resource.service.mapper.ResourceMapper;
import org.springframework.stereotype.Service;

/**
* @author lianghao
* @description 针对表【resource(资源主表，包含标题、上传人、访问权限等元信息)】的数据库操作Service实现
* @createDate 2025-06-23 15:46:05
*/
@Service
public class ResourceServiceImpl extends ServiceImpl<ResourceMapper, Resource>
    implements ResourceService{

}




