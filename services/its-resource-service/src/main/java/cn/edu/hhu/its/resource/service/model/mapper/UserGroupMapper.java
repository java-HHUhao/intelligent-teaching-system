package cn.edu.hhu.its.resource.service.model.mapper;

import cn.edu.hhu.its.resource.service.model.domain.UserGroupDO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户组数据库操作Mapper
 */
@Mapper
public interface UserGroupMapper extends BaseMapper<UserGroupDO> {
} 