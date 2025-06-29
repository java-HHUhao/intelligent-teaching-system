package cn.edu.hhu.its.user.service.model.mapper;

import cn.edu.hhu.its.user.service.model.domain.UserRoleDO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author lianghao
* @description 针对表【user_role(用户角色关系表)】的数据库操作Mapper
* @createDate 2025-06-23 15:50:17
* @Entity cn.edu.hhu.its.user.service.model.domain.UserRole
*/
@Mapper
public interface UserRoleMapper extends BaseMapper<UserRoleDO> {

}




