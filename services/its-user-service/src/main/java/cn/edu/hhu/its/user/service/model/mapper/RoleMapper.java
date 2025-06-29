package cn.edu.hhu.its.user.service.model.mapper;

import cn.edu.hhu.its.user.service.model.domain.RoleDO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author lianghao
* @description 针对表【role(系统角色表)】的数据库操作Mapper
* @createDate 2025-06-23 15:50:02
* @Entity cn.edu.hhu.its.user.service.model.domain.Role
*/
@Mapper
public interface RoleMapper extends BaseMapper<RoleDO> {

}




