package cn.edu.hhu.its.resource.service.controller;

import cn.edu.hhu.its.resource.service.model.dto.request.*;
import cn.edu.hhu.its.resource.service.model.dto.response.ResourceDetailResponseDTO;
import cn.edu.hhu.its.resource.service.model.dto.response.ResourceListResponseDTO;
import cn.edu.hhu.its.resource.service.service.ResourceManagementService;
import cn.edu.hhu.spring.boot.starter.common.result.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * 资源管理接口 [[memory:2478388]]
 */
@Tag(name = "资源管理接口", description = "处理资源列表、详情、编辑、删除等操作")
@RestController
@RequestMapping("/resource/resource")
@RequiredArgsConstructor
public class ResourceManagementController {
    
    private final ResourceManagementService resourceManagementService;
    
    @Operation(summary = "获取个人资源列表")
    @PostMapping("/list")
    public Result<ResourceListResponseDTO> getResourceList(@RequestBody ResourceListRequestDTO requestDTO) {
        return resourceManagementService.getResourceList(requestDTO);
    }
    
    @Operation(summary = "获取单一资源详情")
    @PostMapping("/detail")
    public Result<ResourceDetailResponseDTO> getResourceDetail(@RequestBody ResourceDetailRequestDTO requestDTO) {
        return resourceManagementService.getResourceDetail(requestDTO);
    }
    
    @Operation(summary = "编辑资源信息")
    @PostMapping("/edit")
    public Result<Void> editResource(@RequestBody ResourceEditRequestDTO requestDTO) {
        return resourceManagementService.editResource(requestDTO);
    }
    
    @Operation(summary = "删除资源")
    @DeleteMapping("/delete")
    public Result<Void> deleteResource(
            @Parameter(description = "资源ID") @RequestParam Long resourceId,
            @Parameter(description = "用户ID") @RequestParam Long userId) {
        return resourceManagementService.deleteResource(resourceId, userId);
    }
    
    @Operation(summary = "下载资源")
    @GetMapping("/download")
    public ResponseEntity<InputStreamResource> downloadResource(
            @Parameter(description = "资源ID") @RequestParam Long resourceId,
            @Parameter(description = "用户ID") @RequestParam Long userId) {
        return resourceManagementService.downloadResource(resourceId, userId);
    }
    
    @Operation(summary = "迁移资源到指定目录")
    @PostMapping("/move")
    public Result<Void> moveResource(@RequestBody ResourceMoveRequestDTO requestDTO) {
        return resourceManagementService.moveResource(requestDTO);
    }
    
    @Operation(summary = "重命名资源")
    @PostMapping("/rename")
    public Result<Void> renameResource(@RequestBody ResourceRenameRequestDTO requestDTO) {
        return resourceManagementService.renameResource(requestDTO);
    }
    
    @Operation(summary = "编辑资源内容")
    @PostMapping("/content/edit")
    public Result<Void> editResourceContent(@ModelAttribute ResourceContentEditRequestDTO requestDTO) {
        return resourceManagementService.editResourceContent(requestDTO);
    }
    
    @Operation(summary = "收藏/取消收藏资源")
    @PostMapping("/favorite")
    public Result<Void> favoriteResource(@RequestBody ResourceFavoriteRequestDTO requestDTO) {
        return resourceManagementService.favoriteResource(requestDTO);
    }
} 