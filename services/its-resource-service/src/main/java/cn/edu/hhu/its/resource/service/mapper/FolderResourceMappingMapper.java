package cn.edu.hhu.its.resource.service.mapper;

import cn.edu.hhu.its.resource.service.model.domain.FolderResourceMapping;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author lianghao
* @description 针对表【folder_resource_mapping(文件夹与资源之间的映射关系表)】的数据库操作Mapper
* @createDate 2025-06-23 15:45:59
* @Entity cn.edu.hhu.its.resource.service.model.domain.FolderResourceMapping
*/
@Mapper
public interface FolderResourceMappingMapper extends BaseMapper<FolderResourceMapping> {

}




