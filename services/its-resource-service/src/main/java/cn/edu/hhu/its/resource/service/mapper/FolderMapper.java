package cn.edu.hhu.its.resource.service.mapper;

import cn.edu.hhu.its.resource.service.model.domain.Folder;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author lianghao
* @description 针对表【folder(文件夹表，支持用户自定义的多级目录结构)】的数据库操作Mapper
* @createDate 2025-06-23 15:45:48
* @Entity cn.edu.hhu.its.resource.service.model.domain.Folder
*/
@Mapper
public interface FolderMapper extends BaseMapper<Folder> {

}




