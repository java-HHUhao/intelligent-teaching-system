package cn.edu.hhu.its.resource.service.model.mapper;

import cn.edu.hhu.its.resource.service.model.domain.UserFavoriteDO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author lianghao
* @description 针对表【user_favorite】的数据库操作Mapper
* @createDate 2025-07-08 20:09:58
* @Entity cn.edu.hhu.its.resource.service.model.domain.UserFavoriteDO
*/
@Mapper
public interface UserFavoriteMapper extends BaseMapper<UserFavoriteDO> {

}




