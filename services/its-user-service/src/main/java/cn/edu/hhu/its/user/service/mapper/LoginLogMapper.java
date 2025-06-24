package cn.edu.hhu.its.user.service.mapper;

import cn.edu.hhu.its.user.service.model.domain.LoginLog;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author lianghao
* @description 针对表【login_log(用户登录日志表)】的数据库操作Mapper
* @createDate 2025-06-23 15:49:55
* @Entity cn.edu.hhu.its.user.service.model.domain.LoginLog
*/
@Mapper
public interface LoginLogMapper extends BaseMapper<LoginLog> {

}




