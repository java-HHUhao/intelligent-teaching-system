package cn.edu.hhu.its.resource.service.model.mapper;

import cn.edu.hhu.its.resource.service.model.domain.GroupResourceDO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author lianghao
* @description 针对表【group_resource(用户组与资源的关联表，支持组共享资源)】的数据库操作Mapper
* @createDate 2025-06-23 15:46:02
* @Entity cn.edu.hhu.its.resource.service.model.domain.GroupResourceDO
*/
@Mapper
public interface GroupResourceMapper extends BaseMapper<GroupResourceDO> {

}




