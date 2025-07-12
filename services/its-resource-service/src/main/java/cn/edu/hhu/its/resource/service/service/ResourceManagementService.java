package cn.edu.hhu.its.resource.service.service;

import cn.edu.hhu.its.resource.service.model.dto.request.*;
import cn.edu.hhu.its.resource.service.model.dto.response.ResourceDetailResponseDTO;
import cn.edu.hhu.its.resource.service.model.dto.response.ResourceListResponseDTO;
import cn.edu.hhu.spring.boot.starter.common.result.Result;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;

/**
 * 资源管理服务接口
 */
public interface ResourceManagementService {
    
    /**
     * 获取个人资源列表
     *
     * @param requestDTO 请求参数
     * @return 资源列表
     */
    Result<ResourceListResponseDTO> getResourceList(ResourceListRequestDTO requestDTO);
    
    /**
     * 获取单一资源详情
     *
     * @param requestDTO 请求参数
     * @return 资源详情
     */
    Result<ResourceDetailResponseDTO> getResourceDetail(ResourceDetailRequestDTO requestDTO);
    
    /**
     * 编辑资源信息
     *
     * @param requestDTO 请求参数
     * @return 操作结果
     */
    Result<Void> editResource(ResourceEditRequestDTO requestDTO);
    
    /**
     * 删除资源
     *
     * @param resourceId 资源ID
     * @param userId     用户ID
     * @return 操作结果
     */
    Result<Void> deleteResource(Long resourceId, Long userId);
    
    /**
     * 下载资源
     *
     * @param resourceId 资源ID
     * @param userId     用户ID
     * @return 文件下载响应
     */
    ResponseEntity<InputStreamResource> downloadResource(Long resourceId, Long userId);
    
    /**
     * 迁移资源到指定目录
     *
     * @param requestDTO 请求参数
     * @return 操作结果
     */
    Result<Void> moveResource(ResourceMoveRequestDTO requestDTO);
    
    /**
     * 重命名资源
     *
     * @param requestDTO 请求参数
     * @return 操作结果
     */
    Result<Void> renameResource(ResourceRenameRequestDTO requestDTO);
    
    /**
     * 编辑资源内容
     *
     * @param requestDTO 请求参数
     * @return 操作结果
     */
    Result<Void> editResourceContent(ResourceContentEditRequestDTO requestDTO);
    
    /**
     * 收藏/取消收藏资源
     *
     * @param requestDTO 请求参数
     * @return 操作结果
     */
    Result<Void> favoriteResource(ResourceFavoriteRequestDTO requestDTO);
} 