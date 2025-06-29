package cn.edu.hhu.its.user.service.model.mapper;

import cn.edu.hhu.its.user.service.model.domain.PermissionDO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author lianghao
* @description 针对表【permission(系统权限表)】的数据库操作Mapper
* @createDate 2025-06-23 15:49:59
* @Entity cn.edu.hhu.its.user.service.model.domain.Permission
*/
@Mapper
public interface PermissionMapper extends BaseMapper<PermissionDO> {

}




