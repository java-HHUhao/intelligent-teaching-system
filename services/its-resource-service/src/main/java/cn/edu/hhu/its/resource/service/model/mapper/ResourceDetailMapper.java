package cn.edu.hhu.its.resource.service.model.mapper;

import cn.edu.hhu.its.resource.service.model.domain.ResourceDetailDO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author lianghao
* @description 针对表【resource_detail(资源详情表，记录资源描述与扩展信息)】的数据库操作Mapper
* @createDate 2025-06-23 15:46:08
* @Entity cn.edu.hhu.its.resource.service.model.domain.ResourceDetailDO
*/
@Mapper
public interface ResourceDetailMapper extends BaseMapper<ResourceDetailDO> {

}




