package cn.edu.hhu.its.user.service.model.mapper;

import cn.edu.hhu.its.user.service.model.domain.UserGroupMappingDO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author lianghao
* @description 针对表【user_group_mapping(用户与用户组的多对多关联表)】的数据库操作Mapper
* @createDate 2025-06-23 15:50:14
* @Entity cn.edu.hhu.its.user.service.model.domain.UserGroupMapping
*/
@Mapper
public interface UserGroupMappingMapper extends BaseMapper<UserGroupMappingDO> {

}




