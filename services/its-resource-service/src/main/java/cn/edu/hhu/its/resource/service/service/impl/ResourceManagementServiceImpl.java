package cn.edu.hhu.its.resource.service.service.impl;

import cn.edu.hhu.its.resource.service.common.enums.ResourceErrorCode;
import cn.edu.hhu.its.resource.service.model.domain.FolderDO;
import cn.edu.hhu.its.resource.service.model.domain.FolderResourceMappingDO;
import cn.edu.hhu.its.resource.service.model.domain.ResourceDO;
import cn.edu.hhu.its.resource.service.model.domain.ResourceDetailDO;
import cn.edu.hhu.its.resource.service.model.domain.UserFavoriteDO;
import cn.edu.hhu.its.resource.service.model.dto.request.*;
import cn.edu.hhu.its.resource.service.model.dto.response.ResourceDetailResponseDTO;
import cn.edu.hhu.its.resource.service.model.dto.response.ResourceListResponseDTO;
import cn.edu.hhu.its.resource.service.model.mapper.FolderMapper;
import cn.edu.hhu.its.resource.service.model.mapper.FolderResourceMappingMapper;
import cn.edu.hhu.its.resource.service.model.mapper.ResourceDetailMapper;
import cn.edu.hhu.its.resource.service.model.mapper.ResourceMapper;
import cn.edu.hhu.its.resource.service.model.mapper.UserFavoriteMapper;
import cn.edu.hhu.its.resource.service.service.ResourceManagementService;
import cn.edu.hhu.spring.boot.starter.common.result.Result;
import cn.edu.hhu.spring.boot.starter.common.utils.ResultUtil;
import cn.edu.hhu.spring.boot.starter.store.constant.MinioConstant;
import cn.edu.hhu.spring.boot.starter.store.utils.MinioUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.io.InputStream;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 资源管理服务实现类 [[memory:2478388]]
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ResourceManagementServiceImpl implements ResourceManagementService {
    
    private final ResourceMapper resourceMapper;
    private final ResourceDetailMapper resourceDetailMapper;
    private final FolderResourceMappingMapper folderResourceMappingMapper;
    private final FolderMapper folderMapper;
    private final UserFavoriteMapper userFavoriteMapper;
    
    @Override
    public Result<ResourceListResponseDTO> getResourceList(ResourceListRequestDTO requestDTO) {
        try {
            // 构建查询条件
            LambdaQueryWrapper<ResourceDO> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(ResourceDO::getUserId, requestDTO.getUserId());
            
            // 如果指定了文件夹，查询该文件夹下的资源
            if (requestDTO.getFolderId() != null) {
                // 先查询文件夹资源映射
                List<FolderResourceMappingDO> mappings = folderResourceMappingMapper.selectList(
                        new LambdaQueryWrapper<FolderResourceMappingDO>()
                                .eq(FolderResourceMappingDO::getFolderId, requestDTO.getFolderId())
                );
                
                if (mappings.isEmpty()) {
                    return ResultUtil.success(new ResourceListResponseDTO());
                }
                
                List<Long> resourceIds = mappings.stream()
                        .map(FolderResourceMappingDO::getResourceId)
                        .collect(Collectors.toList());
                wrapper.in(ResourceDO::getId, resourceIds);
            }
            
            // 关键词搜索
            if (StringUtils.hasText(requestDTO.getKeyword())) {
                wrapper.like(ResourceDO::getTitle, requestDTO.getKeyword());
            }
            
            // 类型过滤
            if (StringUtils.hasText(requestDTO.getType())) {
                wrapper.eq(ResourceDO::getType, requestDTO.getType());
            }
            
            wrapper.orderByDesc(ResourceDO::getUpdatedAt);
            
            // 分页查询
            Page<ResourceDO> page = new Page<>(requestDTO.getPageNum(), requestDTO.getPageSize());
            IPage<ResourceDO> result = resourceMapper.selectPage(page, wrapper);
            
            // 转换为响应DTO
            ResourceListResponseDTO responseDTO = new ResourceListResponseDTO();
            responseDTO.setTotal(result.getTotal());
            responseDTO.setPageNum(requestDTO.getPageNum());
            responseDTO.setPageSize(requestDTO.getPageSize());
            
            List<ResourceListResponseDTO.ResourceItem> resourceItems = result.getRecords().stream()
                    .map(this::convertToResourceItem)
                    .collect(Collectors.toList());
            responseDTO.setResources(resourceItems);
            
            // 如果查询特定文件夹，还需要返回该文件夹下的子文件夹
            if (requestDTO.getFolderId() != null) {
                List<ResourceListResponseDTO.FolderItem> folderItems = getFolderItems(requestDTO.getFolderId(), requestDTO.getUserId());
                responseDTO.setFolders(folderItems);
            }
            
            return ResultUtil.success(responseDTO);
        } catch (Exception e) {
            log.error("获取资源列表失败", e);
            return ResultUtil.fail(ResourceErrorCode.OPERATION_FAILED);
        }
    }
    
    @Override
    public Result<ResourceDetailResponseDTO> getResourceDetail(ResourceDetailRequestDTO requestDTO) {
        try {
            // 查询资源基本信息
            ResourceDO resource = resourceMapper.selectById(requestDTO.getResourceId());
            if (resource == null) {
                return ResultUtil.fail(ResourceErrorCode.RESOURCE_NOT_FOUND);
            }
            
            // 权限检查
            if (!hasResourceAccess(resource, requestDTO.getUserId())) {
                return ResultUtil.fail(ResourceErrorCode.RESOURCE_ACCESS_DENIED);
            }
            
            // 查询资源详情
            ResourceDetailDO detail = resourceDetailMapper.selectOne(
                    new LambdaQueryWrapper<ResourceDetailDO>()
                            .eq(ResourceDetailDO::getResourceId, requestDTO.getResourceId())
            );
            
            // 转换为响应DTO
            ResourceDetailResponseDTO responseDTO = new ResourceDetailResponseDTO();
            BeanUtils.copyProperties(resource, responseDTO);
            
            if (detail != null) {
                responseDTO.setFilePath(detail.getFilePath());
                responseDTO.setDescription(detail.getDescription());
                responseDTO.setMetadata(detail.getMetadata() != null ? detail.getMetadata().toString() : null);
                
                // 生成下载URL
                try {
                    String downloadUrl = MinioUtils.getFileUrl(getMinIOBucket(resource.getVisibility()), detail.getFilePath());
                    responseDTO.setDownloadUrl(downloadUrl);
                } catch (Exception e) {
                    log.warn("生成下载URL失败: {}", e.getMessage());
                }
            }
            
            return ResultUtil.success(responseDTO);
        } catch (Exception e) {
            log.error("获取资源详情失败", e);
            return ResultUtil.fail(ResourceErrorCode.OPERATION_FAILED);
        }
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<Void> editResource(ResourceEditRequestDTO requestDTO) {
        try {
            // 查询资源
            ResourceDO resource = resourceMapper.selectById(requestDTO.getResourceId());
            if (resource == null) {
                return ResultUtil.fail(ResourceErrorCode.RESOURCE_NOT_FOUND);
            }
            
            // 权限检查
            if (!Objects.equals(resource.getUserId(), requestDTO.getUserId())) {
                return ResultUtil.fail(ResourceErrorCode.RESOURCE_ACCESS_DENIED);
            }
            
            // 更新资源基本信息
            if (StringUtils.hasText(requestDTO.getTitle())) {
                resource.setTitle(requestDTO.getTitle());
            }
            if (requestDTO.getVisibility() != null) {
                resource.setVisibility(requestDTO.getVisibility());
            }
            resource.setUpdatedAt(new Date());
            
            resourceMapper.updateById(resource);
            
            // 更新详情信息
            if (StringUtils.hasText(requestDTO.getDescription()) || StringUtils.hasText(requestDTO.getMetadata())) {
                ResourceDetailDO detail = resourceDetailMapper.selectOne(
                        new LambdaQueryWrapper<ResourceDetailDO>()
                                .eq(ResourceDetailDO::getResourceId, requestDTO.getResourceId())
                );
                
                if (detail != null) {
                    if (StringUtils.hasText(requestDTO.getDescription())) {
                        detail.setDescription(requestDTO.getDescription());
                    }
                    if (StringUtils.hasText(requestDTO.getMetadata())) {
                        detail.setMetadata(requestDTO.getMetadata());
                    }
                    resourceDetailMapper.updateById(detail);
                }
            }
            
            return ResultUtil.success();
        } catch (Exception e) {
            log.error("编辑资源失败", e);
            return ResultUtil.fail(ResourceErrorCode.OPERATION_FAILED);
        }
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<Void> deleteResource(Long resourceId, Long userId) {
        try {
            // 查询资源
            ResourceDO resource = resourceMapper.selectById(resourceId);
            if (resource == null) {
                return ResultUtil.fail(ResourceErrorCode.RESOURCE_NOT_FOUND);
            }
            
            // 权限检查
            if (!Objects.equals(resource.getUserId(), userId)) {
                return ResultUtil.fail(ResourceErrorCode.RESOURCE_ACCESS_DENIED);
            }
            
            // 查询资源详情
            ResourceDetailDO detail = resourceDetailMapper.selectOne(
                    new LambdaQueryWrapper<ResourceDetailDO>()
                            .eq(ResourceDetailDO::getResourceId, resourceId)
            );
            
            // 删除MinIO中的文件
            if (detail != null && StringUtils.hasText(detail.getFilePath())) {
                try {
                    MinioUtils.deleteFile(getMinIOBucket(resource.getVisibility()), detail.getFilePath());
                } catch (Exception e) {
                    log.warn("删除MinIO文件失败: {}", e.getMessage());
                }
            }
            
            // 删除数据库记录
            resourceMapper.deleteById(resourceId);
            if (detail != null) {
                resourceDetailMapper.deleteById(detail.getId());
            }
            
            // 删除文件夹映射关系
            folderResourceMappingMapper.delete(
                    new LambdaQueryWrapper<FolderResourceMappingDO>()
                            .eq(FolderResourceMappingDO::getResourceId, resourceId)
            );
            
            return ResultUtil.success();
        } catch (Exception e) {
            log.error("删除资源失败", e);
            return ResultUtil.fail(ResourceErrorCode.RESOURCE_DELETE_FAILED);
        }
    }
    
    @Override
    public ResponseEntity<InputStreamResource> downloadResource(Long resourceId, Long userId) {
        try {
            // 查询资源
            ResourceDO resource = resourceMapper.selectById(resourceId);
            if (resource == null) {
                return ResponseEntity.notFound().build();
            }
            
            // 权限检查
            if (!hasResourceAccess(resource, userId)) {
                return ResponseEntity.status(403).build();
            }
            
            // 查询资源详情
            ResourceDetailDO detail = resourceDetailMapper.selectOne(
                    new LambdaQueryWrapper<ResourceDetailDO>()
                            .eq(ResourceDetailDO::getResourceId, resourceId)
            );
            
            if (detail == null || !StringUtils.hasText(detail.getFilePath())) {
                return ResponseEntity.notFound().build();
            }
            
            // 从MinIO获取文件流
            InputStream inputStream = MinioUtils.getFileStream(
                    getMinIOBucket(resource.getVisibility()),
                    detail.getFilePath()
            );
            
            // 构建响应
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, 
                    "attachment; filename=\"" + resource.getTitle() + "\"");
            
            return ResponseEntity.ok()
                    .headers(headers)
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(new InputStreamResource(inputStream));
                    
        } catch (Exception e) {
            log.error("下载资源失败", e);
            return ResponseEntity.status(500).build();
        }
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<Void> moveResource(ResourceMoveRequestDTO requestDTO) {
        try {
            // 验证资源存在且有权限
            ResourceDO resource = resourceMapper.selectById(requestDTO.getResourceId());
            if (resource == null) {
                return ResultUtil.fail(ResourceErrorCode.RESOURCE_NOT_FOUND);
            }
            
            if (!Objects.equals(resource.getUserId(), requestDTO.getUserId())) {
                return ResultUtil.fail(ResourceErrorCode.RESOURCE_ACCESS_DENIED);
            }
            
            // 删除原有映射关系
            folderResourceMappingMapper.delete(
                    new LambdaQueryWrapper<FolderResourceMappingDO>()
                            .eq(FolderResourceMappingDO::getResourceId, requestDTO.getResourceId())
            );
            
            // 如果目标文件夹不为空，创建新的映射关系
            if (requestDTO.getTargetFolderId() != null) {
                FolderResourceMappingDO mapping = new FolderResourceMappingDO();
                mapping.setFolderId(requestDTO.getTargetFolderId());
                mapping.setResourceId(requestDTO.getResourceId());
                mapping.setResourceAlias(StringUtils.hasText(requestDTO.getResourceAlias()) 
                        ? requestDTO.getResourceAlias() : resource.getTitle());
                mapping.setCreatedAt(new Date());
                
                folderResourceMappingMapper.insert(mapping);
            }
            
            return ResultUtil.success();
        } catch (Exception e) {
            log.error("移动资源失败", e);
            return ResultUtil.fail(ResourceErrorCode.RESOURCE_MOVE_FAILED);
        }
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<Void> renameResource(ResourceRenameRequestDTO requestDTO) {
        try {
            // 查询资源
            ResourceDO resource = resourceMapper.selectById(requestDTO.getResourceId());
            if (resource == null) {
                return ResultUtil.fail(ResourceErrorCode.RESOURCE_NOT_FOUND);
            }
            
            // 权限检查
            if (!Objects.equals(resource.getUserId(), requestDTO.getUserId())) {
                return ResultUtil.fail(ResourceErrorCode.RESOURCE_ACCESS_DENIED);
            }
            
            // 更新资源标题
            resource.setTitle(requestDTO.getNewTitle());
            resource.setUpdatedAt(new Date());
            
            resourceMapper.updateById(resource);
            
            return ResultUtil.success();
        } catch (Exception e) {
            log.error("重命名资源失败", e);
            return ResultUtil.fail(ResourceErrorCode.RESOURCE_RENAME_FAILED);
        }
    }
    
    /**
     * 转换为资源项
     */
    private ResourceListResponseDTO.ResourceItem convertToResourceItem(ResourceDO resource) {
        ResourceListResponseDTO.ResourceItem item = new ResourceListResponseDTO.ResourceItem();
        BeanUtils.copyProperties(resource, item);
        
        // 查询文件路径等详情信息
        ResourceDetailDO detail = resourceDetailMapper.selectOne(
                new LambdaQueryWrapper<ResourceDetailDO>()
                        .eq(ResourceDetailDO::getResourceId, resource.getId())
        );
        
        if (detail != null) {
            item.setFilePath(detail.getFilePath());
            // 这里可以添加文件大小等其他信息
        }
        
        return item;
    }
    
    /**
     * 检查资源访问权限
     */
    private boolean hasResourceAccess(ResourceDO resource, Long userId) {
        // 自己的资源可以访问
        if (Objects.equals(resource.getUserId(), userId)) {
            return true;
        }
        
        // 公开资源可以访问
        if (resource.getVisibility() == 2) {
            return true;
        }
        
        // 组共享资源需要检查组权限（这里简化处理）
        if (resource.getVisibility() == 1) {
            // TODO: 实现组权限检查
            return false;
        }
        
        return false;
    }
    
    /**
     * 获取文件夹项列表
     */
    private List<ResourceListResponseDTO.FolderItem> getFolderItems(Long parentId, Long userId) {
        List<FolderDO> folders = folderMapper.selectList(
                new LambdaQueryWrapper<FolderDO>()
                        .eq(FolderDO::getParentId, parentId)
                        .eq(FolderDO::getUserId, userId)
                        .orderByDesc(FolderDO::getUpdatedAt)
        );
        
        return folders.stream()
                .map(this::convertToFolderItem)
                .collect(Collectors.toList());
    }
    
    /**
     * 转换为文件夹项
     */
    private ResourceListResponseDTO.FolderItem convertToFolderItem(FolderDO folder) {
        ResourceListResponseDTO.FolderItem item = new ResourceListResponseDTO.FolderItem();
        BeanUtils.copyProperties(folder, item);
        return item;
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<Void> editResourceContent(ResourceContentEditRequestDTO requestDTO) {
        try {
            // 查询资源
            ResourceDO resource = resourceMapper.selectById(requestDTO.getResourceId());
            if (resource == null) {
                return ResultUtil.fail(ResourceErrorCode.RESOURCE_NOT_FOUND);
            }
            
            // 权限检查
            if (!Objects.equals(resource.getUserId(), requestDTO.getUserId())) {
                return ResultUtil.fail(ResourceErrorCode.RESOURCE_ACCESS_DENIED);
            }
            
            // 查询资源详情
            ResourceDetailDO detail = resourceDetailMapper.selectOne(
                    new LambdaQueryWrapper<ResourceDetailDO>()
                            .eq(ResourceDetailDO::getResourceId, requestDTO.getResourceId())
            );
            
            if (detail == null) {
                return ResultUtil.fail(ResourceErrorCode.RESOURCE_NOT_FOUND);
            }
            
            // 如果有新文件，上传新文件
            if (requestDTO.getNewFile() != null && !requestDTO.getNewFile().isEmpty()) {
                // 生成新文件路径
                String timestamp = java.time.LocalDateTime.now()
                        .format(java.time.format.DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
                String originalFilename = requestDTO.getNewFile().getOriginalFilename();
                String extension = originalFilename != null && originalFilename.contains(".") 
                        ? originalFilename.substring(originalFilename.lastIndexOf(".")) : "";
                String newFilePath = String.format("%d/%s/%s%s", 
                        requestDTO.getUserId(), timestamp, 
                        java.util.UUID.randomUUID().toString(), extension);
                
                // 备份旧文件路径
                String oldFilePath = detail.getFilePath();
                
                try {
                    // 上传新文件
                    MinioUtils.uploadFile(
                            getMinIOBucket(resource.getVisibility()),
                            newFilePath,
                            requestDTO.getNewFile().getInputStream(),
                            requestDTO.getNewFile().getContentType()
                    );
                    
                    // 更新数据库中的文件路径
                    detail.setFilePath(newFilePath);
                    resourceDetailMapper.updateById(detail);
                    
                    // 如果不保留旧文件，删除旧文件
                    if (!requestDTO.getKeepOldFile() && StringUtils.hasText(oldFilePath)) {
                        try {
                            MinioUtils.deleteFile(getMinIOBucket(resource.getVisibility()), oldFilePath);
                        } catch (Exception e) {
                            log.warn("删除旧文件失败: {}", e.getMessage());
                        }
                    }
                    
                    // 更新资源的更新时间
                    resource.setUpdatedAt(new Date());
                    resourceMapper.updateById(resource);
                    
                } catch (Exception e) {
                    log.error("上传新文件失败", e);
                    return ResultUtil.fail(ResourceErrorCode.FILE_UPLOAD_FAILED);
                }
            }
            
            return ResultUtil.success();
        } catch (Exception e) {
            log.error("编辑资源内容失败", e);
            return ResultUtil.fail(ResourceErrorCode.OPERATION_FAILED);
        }
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<Void> favoriteResource(ResourceFavoriteRequestDTO requestDTO) {
        try {
            // 验证参数
            if (requestDTO.getResourceId() == null || requestDTO.getUserId() == null || requestDTO.getFavorite() == null) {
                return ResultUtil.fail(ResourceErrorCode.INVALID_PARAMETER);
            }
            
            // 查询资源是否存在
            ResourceDO resource = resourceMapper.selectById(requestDTO.getResourceId());
            if (resource == null) {
                return ResultUtil.fail(ResourceErrorCode.RESOURCE_NOT_FOUND);
            }
            
            // 检查资源访问权限
            if (!hasResourceAccess(resource, requestDTO.getUserId())) {
                return ResultUtil.fail(ResourceErrorCode.RESOURCE_ACCESS_DENIED);
            }
            
            // 查询是否已经收藏
            UserFavoriteDO existingFavorite = userFavoriteMapper.selectOne(
                    new LambdaQueryWrapper<UserFavoriteDO>()
                            .eq(UserFavoriteDO::getUserId, requestDTO.getUserId())
                            .eq(UserFavoriteDO::getResourceId, requestDTO.getResourceId())
                            .eq(UserFavoriteDO::getIsDeleted, false)
            );
            
            if (requestDTO.getFavorite()) {
                // 收藏操作
                if (existingFavorite != null) {
                    log.info("资源已经收藏，无需重复收藏: userId={}, resourceId={}", 
                            requestDTO.getUserId(), requestDTO.getResourceId());
                    return ResultUtil.success();
                }
                
                // 创建收藏记录
                UserFavoriteDO favorite = new UserFavoriteDO();
                favorite.setUserId(requestDTO.getUserId());
                favorite.setResourceId(requestDTO.getResourceId());
                favorite.setFavoritedAt(new Date());
                favorite.setIsDeleted(false);
                
                userFavoriteMapper.insert(favorite);
                log.info("收藏资源成功: userId={}, resourceId={}", 
                        requestDTO.getUserId(), requestDTO.getResourceId());
                
            } else {
                // 取消收藏操作
                if (existingFavorite == null) {
                    log.info("资源未收藏，无需取消收藏: userId={}, resourceId={}", 
                            requestDTO.getUserId(), requestDTO.getResourceId());
                    return ResultUtil.success();
                }
                
                // 逻辑删除收藏记录
                existingFavorite.setIsDeleted(true);
                userFavoriteMapper.updateById(existingFavorite);
                log.info("取消收藏资源成功: userId={}, resourceId={}", 
                        requestDTO.getUserId(), requestDTO.getResourceId());
            }
            
            return ResultUtil.success();
            
        } catch (Exception e) {
            log.error("收藏资源操作失败: userId={}, resourceId={}, favorite={}", 
                    requestDTO.getUserId(), requestDTO.getResourceId(), requestDTO.getFavorite(), e);
            return ResultUtil.fail(ResourceErrorCode.OPERATION_FAILED);
        }
    }
    
    /**
     * 根据可见性获取MinIO存储桶和路径前缀
     */
    private String getMinIOBucket(Integer visibility) {
        return MinioConstant.ITS_RESOURCE_BUCKET;
    }
} 