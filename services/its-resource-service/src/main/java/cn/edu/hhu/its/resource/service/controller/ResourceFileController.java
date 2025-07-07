package cn.edu.hhu.its.resource.service.controller;

import cn.edu.hhu.its.resource.service.service.ResourceFileService;
import cn.edu.hhu.spring.boot.starter.common.result.Result;
import cn.edu.hhu.spring.boot.starter.common.utils.ResultUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * 文件资源接口
 */
@Tag(name = "文件资源接口", description = "处理文件上传下载等操作")
@RestController
@RequestMapping("/resource/file")
@RequiredArgsConstructor
public class ResourceFileController {
    
    private final ResourceFileService resourceFileService;
    
    @Operation(summary = "上传用户头像")
    @PostMapping("/avatar/upload")
    public Result<String> uploadAvatar(
            @Parameter(description = "用户ID") @RequestParam Long userId,
            @Parameter(description = "头像文件") @RequestParam("file") MultipartFile file) {
        return resourceFileService.uploadAvatar(userId, file);
    }
}