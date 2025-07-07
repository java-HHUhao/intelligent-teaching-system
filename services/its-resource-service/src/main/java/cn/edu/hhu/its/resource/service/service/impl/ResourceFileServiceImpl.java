package cn.edu.hhu.its.resource.service.service.impl;

import cn.edu.hhu.its.resource.service.common.enums.ResourceErrorCode;
import cn.edu.hhu.its.resource.service.service.ResourceFileService;
import cn.edu.hhu.spring.boot.starter.common.result.Result;
import cn.edu.hhu.spring.boot.starter.common.utils.ResultUtil;
import cn.edu.hhu.spring.boot.starter.store.constant.MinioConstant;
import cn.edu.hhu.spring.boot.starter.store.utils.MinioUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

/**
 * 资源文件服务实现类
 */
@Slf4j
@Service
public class ResourceFileServiceImpl implements ResourceFileService {
    
    @Override
    public Result<String> uploadAvatar(Long userId, MultipartFile file) {
        try {
            // 1. 验证文件是否为空
            Result<String> emptyResult = ResultUtil.returnIf(file.isEmpty(), ResourceErrorCode.FILE_EMPTY);
            if (emptyResult != null) {
                return emptyResult;
            }
            
            // 2. 验证文件类型
            String contentType = file.getContentType();
            Result<String> typeResult = ResultUtil.returnIf(
                    contentType == null || !contentType.startsWith("image/"),
                    ResourceErrorCode.FILE_TYPE_NOT_ALLOWED
            );
            if (typeResult != null) {
                return typeResult;
            }
            
            // 3. 生成文件存储路径
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
            String originalFilename = file.getOriginalFilename();
            String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            String objectName = String.format("%d/%s/%s%s", userId, timestamp, UUID.randomUUID().toString(), extension);
            
            // 4. 上传到MinIO临时桶
            MinioUtils.uploadFile(MinioConstant.ITS_TEMP_BUCKET, objectName, file.getInputStream(), file.getContentType());
            
            // 5. 构建访问URL（相对路径，由网关补充完整的访问地址）
            String avatarUrl = String.format("/%s/%s", MinioConstant.ITS_TEMP_BUCKET, objectName);
            return ResultUtil.success(avatarUrl);
            
        } catch (Exception e) {
            log.error("上传头像失败", e);
            return ResultUtil.fail(ResourceErrorCode.FILE_UPLOAD_FAILED);
        }
    }
}
