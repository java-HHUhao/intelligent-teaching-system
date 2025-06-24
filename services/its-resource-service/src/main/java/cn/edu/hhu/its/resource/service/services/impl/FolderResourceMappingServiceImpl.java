package cn.edu.hhu.its.resource.service.services.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.edu.hhu.its.resource.service.model.domain.FolderResourceMapping;
import cn.edu.hhu.its.resource.service.services.FolderResourceMappingService;
import cn.edu.hhu.its.resource.service.mapper.FolderResourceMappingMapper;
import org.springframework.stereotype.Service;

/**
* @author lianghao
* @description 针对表【folder_resource_mapping(文件夹与资源之间的映射关系表)】的数据库操作Service实现
* @createDate 2025-06-23 15:45:59
*/
@Service
public class FolderResourceMappingServiceImpl extends ServiceImpl<FolderResourceMappingMapper, FolderResourceMapping>
    implements FolderResourceMappingService{

}




