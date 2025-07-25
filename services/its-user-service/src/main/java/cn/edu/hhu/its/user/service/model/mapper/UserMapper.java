package cn.edu.hhu.its.user.service.model.mapper;

import cn.edu.hhu.its.user.service.model.domain.UserDO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author lianghao
* @description 针对表【user(系统用户表)】的数据库操作Mapper
* @createDate 2025-06-23 15:50:07
* @Entity cn.edu.hhu.its.user.service.model.domain.UserDO
*/
@Mapper
public interface UserMapper extends BaseMapper<UserDO> {

}




