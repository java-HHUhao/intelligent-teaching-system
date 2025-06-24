package cn.edu.hhu.its.user.service.services.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.edu.hhu.its.user.service.model.domain.LoginLog;
import cn.edu.hhu.its.user.service.services.LoginLogService;
import cn.edu.hhu.its.user.service.mapper.LoginLogMapper;
import org.springframework.stereotype.Service;

/**
* @author lianghao
* @description 针对表【login_log(用户登录日志表)】的数据库操作Service实现
* @createDate 2025-06-23 15:49:55
*/
@Service
public class LoginLogServiceImpl extends ServiceImpl<LoginLogMapper, LoginLog>
    implements LoginLogService{

}




