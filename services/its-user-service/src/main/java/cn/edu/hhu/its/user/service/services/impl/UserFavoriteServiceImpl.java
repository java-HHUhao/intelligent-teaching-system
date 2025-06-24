package cn.edu.hhu.its.user.service.services.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.edu.hhu.its.user.service.model.domain.UserFavorite;
import cn.edu.hhu.its.user.service.services.UserFavoriteService;
import cn.edu.hhu.its.user.service.mapper.UserFavoriteMapper;
import org.springframework.stereotype.Service;

/**
* @author lianghao
* @description 针对表【user_favorite(用户收藏表)】的数据库操作Service实现
* @createDate 2025-06-24 08:41:15
*/
@Service
public class UserFavoriteServiceImpl extends ServiceImpl<UserFavoriteMapper, UserFavorite>
    implements UserFavoriteService{

}




