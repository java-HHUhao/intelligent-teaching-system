package cn.edu.hhu.its.resource.service.mapper;

import cn.edu.hhu.its.resource.service.model.domain.Resource;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author lianghao
* @description 针对表【resource(资源主表，包含标题、上传人、访问权限等元信息)】的数据库操作Mapper
* @createDate 2025-06-23 15:46:05
* @Entity cn.edu.hhu.its.resource.service.model.domain.Resource
*/
@Mapper
public interface ResourceMapper extends BaseMapper<Resource> {

}




