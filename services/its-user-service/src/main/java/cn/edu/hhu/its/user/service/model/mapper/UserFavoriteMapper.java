package cn.edu.hhu.its.user.service.model.mapper;

import cn.edu.hhu.its.user.service.model.domain.UserFavoriteDO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author lianghao
* @description 针对表【user_favorite(用户收藏表)】的数据库操作Mapper
* @createDate 2025-06-24 08:41:15
* @Entity cn.edu.hhu.its.user.service.model.domain.UserFavorite
*/
@Mapper
public interface UserFavoriteMapper extends BaseMapper<UserFavoriteDO> {

}




