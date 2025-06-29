package cn.edu.hhu.its.user.service.model.mapper;

import cn.edu.hhu.its.user.service.model.domain.RolePermissionDO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author lianghao
* @description 针对表【role_permission(角色权限关系表)】的数据库操作Mapper
* @createDate 2025-06-23 15:50:05
* @Entity cn.edu.hhu.its.user.service.model.domain.RolePermission
*/
@Mapper
public interface RolePermissionMapper extends BaseMapper<RolePermissionDO> {

}




