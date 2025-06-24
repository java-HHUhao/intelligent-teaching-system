package cn.edu.hhu.its.user.service.mapper;

import cn.edu.hhu.its.user.service.model.domain.UserGroup;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author lianghao
* @description 针对表【user_group(用户组表，定义教学组/班级等逻辑分组)】的数据库操作Mapper
* @createDate 2025-06-23 15:50:12
* @Entity cn.edu.hhu.its.user.service.model.domain.UserGroup
*/
@Mapper
public interface UserGroupMapper extends BaseMapper<UserGroup> {

}




