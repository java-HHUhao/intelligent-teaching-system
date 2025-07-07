package cn.edu.hhu.its.resource.service.model.dto.request;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

/**
 * 头像上传请求参数
 */
@Data
public class AvatarUploadReqDTO {
    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 头像文件
     */
    private MultipartFile file;
} 