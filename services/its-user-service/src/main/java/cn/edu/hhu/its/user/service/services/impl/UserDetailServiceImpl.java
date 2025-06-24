package cn.edu.hhu.its.user.service.services.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.edu.hhu.its.user.service.model.domain.UserDetail;
import cn.edu.hhu.its.user.service.services.UserDetailService;
import cn.edu.hhu.its.user.service.mapper.UserDetailMapper;
import org.springframework.stereotype.Service;

/**
* @author lianghao
* @description 针对表【user_detail(用户详情信息表)】的数据库操作Service实现
* @createDate 2025-06-23 15:50:09
*/
@Service
public class UserDetailServiceImpl extends ServiceImpl<UserDetailMapper, UserDetail>
    implements UserDetailService{

}




