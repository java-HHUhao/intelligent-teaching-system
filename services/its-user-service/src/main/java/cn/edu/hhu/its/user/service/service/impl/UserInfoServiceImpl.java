package cn.edu.hhu.its.user.service.service.impl;

import cn.edu.hhu.its.user.service.common.contant.UserConstant;
import cn.edu.hhu.its.user.service.common.enums.UserErrorCode;
import cn.edu.hhu.its.user.service.model.domain.UserDO;
import cn.edu.hhu.its.user.service.model.domain.UserDetailDO;
import cn.edu.hhu.its.user.service.model.dto.request.UserAccountUpdateReqDTO;
import cn.edu.hhu.its.user.service.model.dto.request.UserProfileUpdateReqDTO;
import cn.edu.hhu.its.user.service.model.dto.request.UsernameUpdateReqDTO;
import cn.edu.hhu.its.user.service.model.dto.response.UserAccountUpdateRespDTO;
import cn.edu.hhu.its.user.service.model.dto.response.UserProfileRespDTO;
import cn.edu.hhu.its.user.service.model.dto.response.UserProfileUpdateRespDTO;
import cn.edu.hhu.its.user.service.model.dto.response.UsernameUpdateRespDTO;
import cn.edu.hhu.its.user.service.model.mapper.UserDetailMapper;
import cn.edu.hhu.its.user.service.model.mapper.UserMapper;
import cn.edu.hhu.its.user.service.service.UserInfoService;
import cn.edu.hhu.its.user.service.util.JwtUtil;
import cn.edu.hhu.spring.boot.starter.common.exception.ClientException;
import cn.edu.hhu.spring.boot.starter.common.utils.ExceptionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserInfoServiceImpl implements UserInfoService {
    private final UserMapper userMapper;
    private final UserDetailMapper userDetailMapper;
    private final StringRedisTemplate redisTemplate;

    @Override
    public UserProfileRespDTO getUserProfile(Long userId) {
        // 获取用户基本信息
        UserDO user = userMapper.selectById(userId);
        ExceptionUtil.throwIf(user == null || Boolean.TRUE.equals(user.getIsDeleted()),
                () -> new ClientException(UserErrorCode.USER_NOT_EXIST));

        // 获取用户详细信息
        UserDetailDO userDetail = userDetailMapper.selectOne(
                new LambdaQueryWrapper<UserDetailDO>()
                        .eq(UserDetailDO::getUserId, userId));
        
        // 如果详细信息不存在，创建一个空对象
        if (userDetail == null) {
            userDetail = new UserDetailDO();
            userDetail.setUserId(userId);
        }

        // 构建返回对象
        return new UserProfileRespDTO()
                .setUserId(user.getId())
                .setUsername(user.getUsername())
                .setEmail(user.getEmail())
                .setPhone(user.getPhone())
                .setStatus(user.getStatus())
                .setCreatedAt(user.getCreatedAt())
                .setAvatarUrl(userDetail.getAvatarUrl())
                .setGender(userDetail.getGender())
                .setBirthday(userDetail.getBirthday())
                .setAddress(userDetail.getAddress())
                .setBio(userDetail.getBio());
    }

    @Override
    @Transactional
    public UserProfileUpdateRespDTO updateUserProfile(Long userId, UserProfileUpdateReqDTO updateReq) {
        // 1. 校验用户是否存在
        UserDO user = userMapper.selectById(userId);
        ExceptionUtil.throwIf(user == null || Boolean.TRUE.equals(user.getIsDeleted()),
                () -> new ClientException(UserErrorCode.USER_NOT_EXIST));

        // 2. 校验是否有字段需要更新
        boolean hasUpdate = false;
        if (updateReq.getGender() != null) hasUpdate = true;
        if (updateReq.getBirthday() != null) hasUpdate = true;
        if (updateReq.getAddress() != null) hasUpdate = true;
        if (updateReq.getBio() != null) hasUpdate = true;

        ExceptionUtil.throwIf(!hasUpdate,
                () -> new ClientException(UserErrorCode.PROFILE_UPDATE_EMPTY));

        // 3. 获取或创建用户详细信息
        UserDetailDO userDetail = userDetailMapper.selectOne(
                new LambdaQueryWrapper<UserDetailDO>()
                        .eq(UserDetailDO::getUserId, userId));
        
        if (userDetail == null) {
            userDetail = new UserDetailDO();
            userDetail.setUserId(userId);
            userDetail.setCreatedAt(new Date());
        }

        // 4. 只更新非空字段
        if (updateReq.getGender() != null) {
            userDetail.setGender(updateReq.getGender());
        }
        if (updateReq.getBirthday() != null) {
            userDetail.setBirthday(updateReq.getBirthday());
        }
        if (updateReq.getAddress() != null) {
            userDetail.setAddress(updateReq.getAddress());
        }
        if (updateReq.getBio() != null) {
            userDetail.setBio(updateReq.getBio());
        }
        userDetail.setUpdatedAt(new Date());

        // 5. 保存更新
        if (userDetail.getId() == null) {
            userDetailMapper.insert(userDetail);
        } else {
            userDetailMapper.updateById(userDetail);
        }

        // 6. 构建返回结果
        return new UserProfileUpdateRespDTO()
                .setGender(userDetail.getGender())
                .setBirthday(userDetail.getBirthday())
                .setAddress(userDetail.getAddress())
                .setBio(userDetail.getBio());
    }

    @Override
    @Transactional
    public UserAccountUpdateRespDTO updateUserAccount(Long userId, UserAccountUpdateReqDTO updateReq) {
        // 1. 校验用户是否存在
        UserDO user = userMapper.selectById(userId);
        ExceptionUtil.throwIf(user == null || Boolean.TRUE.equals(user.getIsDeleted()),
                () -> new ClientException(UserErrorCode.USER_NOT_EXIST));

        // 2. 校验是否有字段需要更新
        boolean hasUpdate = false;
        if (updateReq.getEmail() != null) hasUpdate = true;
        if (updateReq.getPhone() != null) hasUpdate = true;

        ExceptionUtil.throwIf(!hasUpdate,
                () -> new ClientException(UserErrorCode.ACCOUNT_UPDATE_EMPTY));

        // 3. 检查邮箱是否被其他用户使用
        if (updateReq.getEmail() != null && !updateReq.getEmail().equals(user.getEmail())) {
            Long count = userMapper.selectCount(new LambdaQueryWrapper<UserDO>()
                    .eq(UserDO::getEmail, updateReq.getEmail())
                    .ne(UserDO::getId, userId));
            ExceptionUtil.throwIf(count > 0,
                    () -> new ClientException(UserErrorCode.EMAIL_VERIFY_ERROR));
            user.setEmail(updateReq.getEmail());
        }

        // 4. 检查手机号是否被其他用户使用
        if (updateReq.getPhone() != null && !updateReq.getPhone().equals(user.getPhone())) {
            Long count = userMapper.selectCount(new LambdaQueryWrapper<UserDO>()
                    .eq(UserDO::getPhone, updateReq.getPhone())
                    .ne(UserDO::getId, userId));
            ExceptionUtil.throwIf(count > 0,
                    () -> new ClientException(UserErrorCode.PHONE_VERIFY_ERROR));
            user.setPhone(updateReq.getPhone());
        }

        // 5. 更新用户信息
        user.setUpdatedAt(new Date());
        userMapper.updateById(user);

        // 6. 返回更新后的账户信息
        return new UserAccountUpdateRespDTO()
                .setEmail(user.getEmail())
                .setPhone(user.getPhone());
    }

    @Override
    @Transactional
    public UsernameUpdateRespDTO updateUsername(Long userId, UsernameUpdateReqDTO updateReq) {
        // 1. 校验用户是否存在
        UserDO user = userMapper.selectById(userId);
        ExceptionUtil.throwIf(user == null || Boolean.TRUE.equals(user.getIsDeleted()),
                () -> new ClientException(UserErrorCode.USER_NOT_EXIST));

        // 2. 检查新用户名是否已被使用
        Long count = userMapper.selectCount(new LambdaQueryWrapper<UserDO>()
                .eq(UserDO::getUsername, updateReq.getUsername())
                .ne(UserDO::getId, userId));
        ExceptionUtil.throwIf(count > 0,
                () -> new ClientException(UserErrorCode.USERNAME_ALREADY_EXISTS));

        String oldUsername = user.getUsername();
        
        // 3. 更新用户名
        user.setUsername(updateReq.getUsername())
            .setUpdatedAt(new Date());
        userMapper.updateById(user);

        // 4. 删除旧的权限缓存
        String oldPermissionKey = "auth:permission:" + userId;
        redisTemplate.delete(oldPermissionKey);

        // 5. 生成新的token
        Map<String, Object> claims = new HashMap<>();
        claims.put(UserConstant.LOGIN_TOKEN_USER_ID, userId);
        claims.put(UserConstant.LOGIN_TOKEN_USER_NAME, updateReq.getUsername());
        // 设置token过期时间为30天
        String newToken = JwtUtil.generateToken(claims, 30 * 24 * 60 * 60 * 1000L);

        // 6. 删除旧token（如果需要）
        String oldTokenKey = "auth:token:" + oldUsername;
        redisTemplate.delete(oldTokenKey);

        // 7. 缓存新token
        String newTokenKey = "auth:token:" + updateReq.getUsername();
        redisTemplate.opsForValue().set(newTokenKey, newToken, 30, TimeUnit.DAYS);

        // 8. 返回结果
        return new UsernameUpdateRespDTO()
                .setUsername(updateReq.getUsername())
                .setToken(newToken);
    }
}
