package cn.edu.hhu.its.user.service.services.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.edu.hhu.its.user.service.model.domain.User;
import cn.edu.hhu.its.user.service.services.UserService;
import cn.edu.hhu.its.user.service.mapper.UserMapper;
import org.springframework.stereotype.Service;

/**
* @author lianghao
* @description 针对表【user(系统用户表)】的数据库操作Service实现
* @createDate 2025-06-23 15:50:07
*/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService{

}




