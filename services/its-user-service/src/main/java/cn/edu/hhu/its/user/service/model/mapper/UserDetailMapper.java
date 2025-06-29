package cn.edu.hhu.its.user.service.model.mapper;

import cn.edu.hhu.its.user.service.model.domain.UserDetailDO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author lianghao
* @description 针对表【user_detail(用户详情信息表)】的数据库操作Mapper
* @createDate 2025-06-29 22:23:31
* @Entity cn.edu.hhu.its.user.service.model.domain.UserDetailDO
*/
@Mapper
public interface UserDetailMapper extends BaseMapper<UserDetailDO> {

}




