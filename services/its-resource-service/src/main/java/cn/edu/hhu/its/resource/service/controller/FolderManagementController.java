package cn.edu.hhu.its.resource.service.controller;

import cn.edu.hhu.its.resource.service.model.dto.request.CreateFolderRequestDTO;
import cn.edu.hhu.its.resource.service.model.dto.request.FolderListRequestDTO;
import cn.edu.hhu.its.resource.service.model.dto.request.FolderMoveRequestDTO;
import cn.edu.hhu.its.resource.service.model.dto.request.FolderRenameRequestDTO;
import cn.edu.hhu.its.resource.service.model.dto.response.FolderInfoResponseDTO;
import cn.edu.hhu.its.resource.service.model.dto.response.FolderListResponseDTO;
import cn.edu.hhu.its.resource.service.service.FolderManagementService;
import cn.edu.hhu.spring.boot.starter.common.result.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 文件夹管理接口 [[memory:2478388]]
 */
@Tag(name = "文件夹管理接口", description = "处理文件夹创建、删除、重命名等操作")
@RestController
@RequestMapping("/resource/folder")
@RequiredArgsConstructor
public class FolderManagementController {
    
    private final FolderManagementService folderManagementService;
    
    @Operation(summary = "创建文件夹")
    @PostMapping("/create")
    public Result<FolderInfoResponseDTO> createFolder(@RequestBody CreateFolderRequestDTO requestDTO) {
        return folderManagementService.createFolder(requestDTO);
    }
    
    @Operation(summary = "删除文件夹")
    @DeleteMapping("/delete")
    public Result<Void> deleteFolder(
            @Parameter(description = "文件夹ID") @RequestParam Long folderId,
            @Parameter(description = "用户ID") @RequestParam Long userId) {
        return folderManagementService.deleteFolder(folderId, userId);
    }
    
    @Operation(summary = "重命名文件夹")
    @PostMapping("/rename")
    public Result<Void> renameFolder(@RequestBody FolderRenameRequestDTO requestDTO) {
        return folderManagementService.renameFolder(requestDTO);
    }
    
    @Operation(summary = "移动文件夹（递归移动内部文件夹和资源）")
    @PostMapping("/move")
    public Result<Void> moveFolder(@RequestBody FolderMoveRequestDTO requestDTO) {
        return folderManagementService.moveFolder(requestDTO);
    }
    
    @Operation(summary = "获取文件夹列表")
    @PostMapping("/list")
    public Result<FolderListResponseDTO> getFolderList(@RequestBody FolderListRequestDTO requestDTO) {
        return folderManagementService.getFolderList(requestDTO);
    }
} 