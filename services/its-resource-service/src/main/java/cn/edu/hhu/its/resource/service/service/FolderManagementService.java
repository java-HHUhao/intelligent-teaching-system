package cn.edu.hhu.its.resource.service.service;

import cn.edu.hhu.its.resource.service.model.dto.request.CreateFolderRequestDTO;
import cn.edu.hhu.its.resource.service.model.dto.request.FolderListRequestDTO;
import cn.edu.hhu.its.resource.service.model.dto.request.FolderMoveRequestDTO;
import cn.edu.hhu.its.resource.service.model.dto.request.FolderRenameRequestDTO;
import cn.edu.hhu.its.resource.service.model.dto.response.FolderInfoResponseDTO;
import cn.edu.hhu.its.resource.service.model.dto.response.FolderListResponseDTO;
import cn.edu.hhu.spring.boot.starter.common.result.Result;

/**
 * 文件夹管理服务接口
 */
public interface FolderManagementService {
    
    /**
     * 创建文件夹
     *
     * @param requestDTO 请求参数
     * @return 创建的文件夹信息
     */
    Result<FolderInfoResponseDTO> createFolder(CreateFolderRequestDTO requestDTO);
    
    /**
     * 删除文件夹
     *
     * @param folderId 文件夹ID
     * @param userId   用户ID
     * @return 操作结果
     */
    Result<Void> deleteFolder(Long folderId, Long userId);
    
    /**
     * 重命名文件夹
     *
     * @param requestDTO 请求参数
     * @return 操作结果
     */
    Result<Void> renameFolder(FolderRenameRequestDTO requestDTO);
    
    /**
     * 移动文件夹（递归移动内部文件夹和资源）
     *
     * @param requestDTO 请求参数
     * @return 操作结果
     */
    Result<Void> moveFolder(FolderMoveRequestDTO requestDTO);
    
    /**
     * 获取文件夹列表
     *
     * @param requestDTO 请求参数
     * @return 文件夹列表
     */
    Result<FolderListResponseDTO> getFolderList(FolderListRequestDTO requestDTO);
} 