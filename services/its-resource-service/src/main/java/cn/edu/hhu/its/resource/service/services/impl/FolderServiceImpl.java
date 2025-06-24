package cn.edu.hhu.its.resource.service.services.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.edu.hhu.its.resource.service.model.domain.Folder;
import cn.edu.hhu.its.resource.service.services.FolderService;
import cn.edu.hhu.its.resource.service.mapper.FolderMapper;
import org.springframework.stereotype.Service;

/**
* @author lianghao
* @description 针对表【folder(文件夹表，支持用户自定义的多级目录结构)】的数据库操作Service实现
* @createDate 2025-06-23 15:45:48
*/
@Service
public class FolderServiceImpl extends ServiceImpl<FolderMapper, Folder>
    implements FolderService{

}




