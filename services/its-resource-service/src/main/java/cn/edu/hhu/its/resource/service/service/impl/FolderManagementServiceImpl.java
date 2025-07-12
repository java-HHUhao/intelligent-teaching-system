package cn.edu.hhu.its.resource.service.service.impl;

import cn.edu.hhu.its.resource.service.common.enums.ResourceErrorCode;
import cn.edu.hhu.its.resource.service.model.domain.FolderDO;
import cn.edu.hhu.its.resource.service.model.domain.FolderResourceMappingDO;
import cn.edu.hhu.its.resource.service.model.dto.request.CreateFolderRequestDTO;
import cn.edu.hhu.its.resource.service.model.dto.request.FolderListRequestDTO;
import cn.edu.hhu.its.resource.service.model.dto.request.FolderMoveRequestDTO;
import cn.edu.hhu.its.resource.service.model.dto.request.FolderRenameRequestDTO;
import cn.edu.hhu.its.resource.service.model.dto.response.FolderInfoResponseDTO;
import cn.edu.hhu.its.resource.service.model.dto.response.FolderListResponseDTO;
import cn.edu.hhu.its.resource.service.model.mapper.FolderMapper;
import cn.edu.hhu.its.resource.service.model.mapper.FolderResourceMappingMapper;
import cn.edu.hhu.its.resource.service.service.FolderManagementService;
import cn.edu.hhu.spring.boot.starter.common.result.Result;
import cn.edu.hhu.spring.boot.starter.common.utils.ResultUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 文件夹管理服务实现类 [[memory:2478388]]
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class FolderManagementServiceImpl implements FolderManagementService {
    
    private final FolderMapper folderMapper;
    private final FolderResourceMappingMapper folderResourceMappingMapper;
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<FolderInfoResponseDTO> createFolder(CreateFolderRequestDTO requestDTO) {
        try {
            // 检查同级目录下是否已存在同名文件夹
            LambdaQueryWrapper<FolderDO> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(FolderDO::getUserId, requestDTO.getUserId())
                   .eq(FolderDO::getFolderName, requestDTO.getFolderName());
            
            if (requestDTO.getParentId() != null) {
                wrapper.eq(FolderDO::getParentId, requestDTO.getParentId());
            } else {
                wrapper.isNull(FolderDO::getParentId);
            }
            
            FolderDO existingFolder = folderMapper.selectOne(wrapper);
            if (existingFolder != null) {
                return ResultUtil.fail(ResourceErrorCode.FOLDER_ALREADY_EXISTS);
            }
            
            // 如果有父文件夹，验证父文件夹是否存在且属于当前用户
            if (requestDTO.getParentId() != null) {
                FolderDO parentFolder = folderMapper.selectById(requestDTO.getParentId());
                if (parentFolder == null) {
                    return ResultUtil.fail(ResourceErrorCode.FOLDER_NOT_FOUND);
                }
                if (!Objects.equals(parentFolder.getUserId(), requestDTO.getUserId())) {
                    return ResultUtil.fail(ResourceErrorCode.RESOURCE_ACCESS_DENIED);
                }
            }
            
            // 创建文件夹
            FolderDO folder = new FolderDO();
            folder.setUserId(requestDTO.getUserId());
            folder.setParentId(requestDTO.getParentId());
            folder.setFolderName(requestDTO.getFolderName());
            folder.setCreatedAt(new Date());
            folder.setUpdatedAt(new Date());
            
            folderMapper.insert(folder);
            
            // 返回创建的文件夹信息
            FolderInfoResponseDTO response = new FolderInfoResponseDTO();
            BeanUtils.copyProperties(folder, response);
            return ResultUtil.success(response);
            
        } catch (Exception e) {
            log.error("创建文件夹失败", e);
            return ResultUtil.fail(ResourceErrorCode.FOLDER_CREATE_FAILED);
        }
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<Void> deleteFolder(Long folderId, Long userId) {
        try {
            // 查询文件夹
            FolderDO folder = folderMapper.selectById(folderId);
            if (folder == null) {
                return ResultUtil.fail(ResourceErrorCode.FOLDER_NOT_FOUND);
            }
            
            // 权限检查
            if (!Objects.equals(folder.getUserId(), userId)) {
                return ResultUtil.fail(ResourceErrorCode.RESOURCE_ACCESS_DENIED);
            }
            
            // 检查文件夹是否为空（没有子文件夹和资源）
            if (!isFolderEmpty(folderId)) {
                return ResultUtil.fail(ResourceErrorCode.FOLDER_NOT_EMPTY);
            }
            
            // 删除文件夹
            folderMapper.deleteById(folderId);
            
            return ResultUtil.success();
        } catch (Exception e) {
            log.error("删除文件夹失败", e);
            return ResultUtil.fail(ResourceErrorCode.FOLDER_DELETE_FAILED);
        }
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<Void> renameFolder(FolderRenameRequestDTO requestDTO) {
        try {
            // 查询文件夹
            FolderDO folder = folderMapper.selectById(requestDTO.getFolderId());
            if (folder == null) {
                return ResultUtil.fail(ResourceErrorCode.FOLDER_NOT_FOUND);
            }
            
            // 权限检查
            if (!Objects.equals(folder.getUserId(), requestDTO.getUserId())) {
                return ResultUtil.fail(ResourceErrorCode.RESOURCE_ACCESS_DENIED);
            }
            
            // 检查同级目录下是否已存在同名文件夹
            LambdaQueryWrapper<FolderDO> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(FolderDO::getUserId, requestDTO.getUserId())
                   .eq(FolderDO::getFolderName, requestDTO.getNewFolderName())
                   .ne(FolderDO::getId, requestDTO.getFolderId()); // 排除自己
            
            if (folder.getParentId() != null) {
                wrapper.eq(FolderDO::getParentId, folder.getParentId());
            } else {
                wrapper.isNull(FolderDO::getParentId);
            }
            
            FolderDO existingFolder = folderMapper.selectOne(wrapper);
            if (existingFolder != null) {
                return ResultUtil.fail(ResourceErrorCode.FOLDER_ALREADY_EXISTS);
            }
            
            // 更新文件夹名称
            folder.setFolderName(requestDTO.getNewFolderName());
            folder.setUpdatedAt(new Date());
            
            folderMapper.updateById(folder);
            
            return ResultUtil.success();
        } catch (Exception e) {
            log.error("重命名文件夹失败", e);
            return ResultUtil.fail(ResourceErrorCode.FOLDER_RENAME_FAILED);
        }
    }
    
    /**
     * 检查文件夹是否为空（递归检查子文件夹和资源）
     */
    private boolean isFolderEmpty(Long folderId) {
        // 检查是否有子文件夹
        long subFolderCount = folderMapper.selectCount(
                new LambdaQueryWrapper<FolderDO>()
                        .eq(FolderDO::getParentId, folderId)
        );
        
        if (subFolderCount > 0) {
            return false;
        }
        
        // 检查是否有资源
        long resourceCount = folderResourceMappingMapper.selectCount(
                new LambdaQueryWrapper<FolderResourceMappingDO>()
                        .eq(FolderResourceMappingDO::getFolderId, folderId)
        );
        
        return resourceCount == 0;
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<Void> moveFolder(FolderMoveRequestDTO requestDTO) {
        try {
            // 查询要移动的文件夹
            FolderDO sourceFolder = folderMapper.selectById(requestDTO.getFolderId());
            if (sourceFolder == null) {
                return ResultUtil.fail(ResourceErrorCode.FOLDER_NOT_FOUND);
            }
            
            // 权限检查
            if (!Objects.equals(sourceFolder.getUserId(), requestDTO.getUserId())) {
                return ResultUtil.fail(ResourceErrorCode.RESOURCE_ACCESS_DENIED);
            }
            
            // 检查目标位置是否合法
            if (requestDTO.getTargetParentId() != null) {
                // 验证目标父文件夹存在且属于当前用户
                FolderDO targetParentFolder = folderMapper.selectById(requestDTO.getTargetParentId());
                if (targetParentFolder == null) {
                    return ResultUtil.fail(ResourceErrorCode.FOLDER_NOT_FOUND);
                }
                if (!Objects.equals(targetParentFolder.getUserId(), requestDTO.getUserId())) {
                    return ResultUtil.fail(ResourceErrorCode.RESOURCE_ACCESS_DENIED);
                }
                
                // 检查是否试图将文件夹移动到自己的子文件夹中（避免循环引用）
                if (isDescendantFolder(requestDTO.getTargetParentId(), requestDTO.getFolderId())) {
                    return ResultUtil.fail(ResourceErrorCode.INVALID_PARAMETER);
                }
            }
            
            // 检查目标位置是否已存在同名文件夹
            if (isFolderNameExistsInTarget(sourceFolder.getFolderName(), requestDTO.getTargetParentId(), requestDTO.getUserId(), requestDTO.getFolderId())) {
                return ResultUtil.fail(ResourceErrorCode.FOLDER_ALREADY_EXISTS);
            }
            
            // 递归移动文件夹及其内容
            moveFolderRecursive(requestDTO.getFolderId(), requestDTO.getTargetParentId());
            
            return ResultUtil.success();
        } catch (Exception e) {
            log.error("移动文件夹失败", e);
            return ResultUtil.fail(ResourceErrorCode.OPERATION_FAILED);
        }
    }
    
    /**
     * 递归移动文件夹及其内容
     */
    private void moveFolderRecursive(Long folderId, Long newParentId) {
        // 1. 移动当前文件夹
        FolderDO folder = folderMapper.selectById(folderId);
        folder.setParentId(newParentId);
        folder.setUpdatedAt(new Date());
        folderMapper.updateById(folder);
        
        // 2. 获取并移动所有子文件夹
        List<FolderDO> subFolders = folderMapper.selectList(
                new LambdaQueryWrapper<FolderDO>()
                        .eq(FolderDO::getParentId, folderId)
        );
        
        for (FolderDO subFolder : subFolders) {
            // 递归移动子文件夹（保持相对层级关系）
            moveFolderRecursive(subFolder.getId(), folderId);
        }
        
        // 注意：文件夹内的资源不需要特别处理，因为它们通过folder_resource_mapping表关联
        // 移动文件夹后，资源仍然通过映射关系属于该文件夹
    }
    
    /**
     * 检查目标文件夹是否是源文件夹的后代（避免循环引用）
     */
    private boolean isDescendantFolder(Long targetParentId, Long sourceFolderId) {
        if (targetParentId == null) {
            return false;
        }
        
        if (Objects.equals(targetParentId, sourceFolderId)) {
            return true;
        }
        
        FolderDO targetFolder = folderMapper.selectById(targetParentId);
        if (targetFolder == null || targetFolder.getParentId() == null) {
            return false;
        }
        
        return isDescendantFolder(targetFolder.getParentId(), sourceFolderId);
    }
    
    /**
     * 检查目标位置是否已存在同名文件夹
     */
    private boolean isFolderNameExistsInTarget(String folderName, Long targetParentId, Long userId, Long excludeFolderId) {
        LambdaQueryWrapper<FolderDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(FolderDO::getUserId, userId)
               .eq(FolderDO::getFolderName, folderName)
               .ne(FolderDO::getId, excludeFolderId);
        
        if (targetParentId != null) {
            wrapper.eq(FolderDO::getParentId, targetParentId);
        } else {
            wrapper.isNull(FolderDO::getParentId);
        }
        
        return folderMapper.selectOne(wrapper) != null;
    }
    
    @Override
    public Result<FolderListResponseDTO> getFolderList(FolderListRequestDTO requestDTO) {
        try {
            // 构建查询条件
            LambdaQueryWrapper<FolderDO> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(FolderDO::getUserId, requestDTO.getUserId());
            
            if (requestDTO.getParentId() != null) {
                wrapper.eq(FolderDO::getParentId, requestDTO.getParentId());
            } else {
                wrapper.isNull(FolderDO::getParentId);
            }
            
            // 关键词搜索
            if (StringUtils.hasText(requestDTO.getKeyword())) {
                wrapper.like(FolderDO::getFolderName, requestDTO.getKeyword());
            }
            
            wrapper.orderByDesc(FolderDO::getUpdatedAt);
            
            // 分页查询
            Page<FolderDO> page = new Page<>(requestDTO.getPageNum(), requestDTO.getPageSize());
            IPage<FolderDO> result = folderMapper.selectPage(page, wrapper);
            
            // 转换为响应DTO
            FolderListResponseDTO responseDTO = new FolderListResponseDTO();
            responseDTO.setTotal(result.getTotal());
            responseDTO.setPageNum(requestDTO.getPageNum());
            responseDTO.setPageSize(requestDTO.getPageSize());
            
            List<FolderListResponseDTO.FolderItem> folderItems = result.getRecords().stream()
                    .map(this::convertToFolderItem)
                    .collect(Collectors.toList());
            responseDTO.setFolders(folderItems);
            
            return ResultUtil.success(responseDTO);
        } catch (Exception e) {
            log.error("获取文件夹列表失败", e);
            return ResultUtil.fail(ResourceErrorCode.OPERATION_FAILED);
        }
    }
    
    /**
     * 转换为文件夹项
     */
    private FolderListResponseDTO.FolderItem convertToFolderItem(FolderDO folder) {
        FolderListResponseDTO.FolderItem item = new FolderListResponseDTO.FolderItem();
        BeanUtils.copyProperties(folder, item);
        
        // 统计子文件夹数量
        long subFolderCount = folderMapper.selectCount(
                new LambdaQueryWrapper<FolderDO>()
                        .eq(FolderDO::getParentId, folder.getId())
        );
        item.setSubFolderCount((int) subFolderCount);
        
        // 统计资源数量
        long resourceCount = folderResourceMappingMapper.selectCount(
                new LambdaQueryWrapper<FolderResourceMappingDO>()
                        .eq(FolderResourceMappingDO::getFolderId, folder.getId())
        );
        item.setResourceCount((int) resourceCount);
        
        return item;
    }
} 