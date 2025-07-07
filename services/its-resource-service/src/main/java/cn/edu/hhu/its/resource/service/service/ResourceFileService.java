package cn.edu.hhu.its.resource.service.service;

import cn.edu.hhu.spring.boot.starter.common.result.Result;
import org.springframework.web.multipart.MultipartFile;

/**
 * 资源文件服务接口
 */
public interface ResourceFileService {
    
    /**
     * 上传用户头像
     *
     * @param userId 用户ID
     * @param file   头像文件
     * @return 头像URL
     */
    Result<String> uploadAvatar(Long userId, MultipartFile file);
}
