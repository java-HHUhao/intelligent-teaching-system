package cn.edu.hhu.its.user.service.service.impl;

import cn.edu.hhu.its.user.service.common.cache.UserCachePrefix;
import cn.edu.hhu.its.user.service.common.enums.UserErrorCode;
import cn.edu.hhu.its.user.service.model.domain.LoginLogDO;
import cn.edu.hhu.its.user.service.model.domain.UserDO;
import cn.edu.hhu.its.user.service.model.domain.UserDetailDO;
import cn.edu.hhu.its.user.service.model.dto.req.UserLoginReqDTO;
import cn.edu.hhu.its.user.service.model.dto.req.UserRegisterReqDTO;
import cn.edu.hhu.its.user.service.model.dto.resp.UserLoginRespDTO;
import cn.edu.hhu.its.user.service.model.dto.resp.UserRegisterRespDTO;
import cn.edu.hhu.its.user.service.model.mapper.LoginLogMapper;
import cn.edu.hhu.its.user.service.model.mapper.UserDetailMapper;
import cn.edu.hhu.its.user.service.model.mapper.UserMapper;
import cn.edu.hhu.its.user.service.service.UserService;
import cn.edu.hhu.its.user.service.util.JWTUtil;
import cn.edu.hhu.spring.boot.starter.common.exception.ClientException;
import cn.edu.hhu.spring.boot.starter.common.utils.ExceptionUtil;
import cn.edu.hhu.spring.boot.starter.distributedid.handler.IdGeneratorManager;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static cn.edu.hhu.its.user.service.common.contant.UserConstant.USER_SERVICE_ID;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserMapper userMapper;
    private final UserDetailMapper userDetailMapper;
    private final LoginLogMapper loginLogMapper;
    private final RedisTemplate<String, Object> redisTemplate;

    @Override
    public UserLoginRespDTO login(UserLoginReqDTO userLoginReqDTO) {
        String input = userLoginReqDTO.getUsernameOrMailOrPhone();
        String password = userLoginReqDTO.getPassword();

        // 判断账号类型
        boolean isEmail = input.contains("@");
        boolean isPhone = input.matches("^\\d{11}$");
        boolean isUsername = !isEmail || !isPhone;


        // 数据库查找
        LambdaQueryWrapper<UserDO> queryWrapper = new LambdaQueryWrapper<>();
        if (isEmail) {
            queryWrapper.eq(UserDO::getEmail, input);
        } else if (isPhone) {
            queryWrapper.eq(UserDO::getPhone, input);
        } else {
            queryWrapper.eq(UserDO::getUsername, input);
        }

        UserDO user = userMapper.selectOne(queryWrapper);
        ExceptionUtil.throwIf(user == null || Boolean.TRUE.equals(user.getIsDeleted()),
                () -> new ClientException(UserErrorCode.USER_NOT_EXIST));

        // 用户被禁用
        ExceptionUtil.throwIf(user.getStatus() == 0,
                () -> new ClientException(UserErrorCode.USER_FORBIDDEN));

        // 密码校验
        ExceptionUtil.throwIf(!password.equals(user.getPassword()),
                () -> new ClientException(UserErrorCode.USER_PASSWORD_ERROR));

        // 生成 JWT
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", user.getId());
        claims.put("username", user.getUsername());

        String token = JWTUtil.generateToken(claims);

        // 写入缓存（6小时）
        String tokenKey=UserCachePrefix.ACCESS_TOKEN+user.getId();
        redisTemplate.opsForValue().set(tokenKey,token, Duration.ofHours(6));

        // 登录日志记录
        LoginLogDO log = new LoginLogDO();
        log.setId(IdGeneratorManager.getDefaultServiceIdGenerator().nextId(USER_SERVICE_ID));
        log.setUserId(user.getId());
        log.setLoginTime(new Date());
        //TODO 传入用户IP
        log.setLoginStatus(1);
        loginLogMapper.insert(log);

        return new UserLoginRespDTO(user.getId(), user.getUsername(), token);
    }

    @Override
    @Transactional
    public UserRegisterRespDTO register(UserRegisterReqDTO userRegisterReqDTO) {
        String username = userRegisterReqDTO.getUsername();
        String password = userRegisterReqDTO.getPassword();
        String confirmPassword = userRegisterReqDTO.getConfirmPassword();
        String email = userRegisterReqDTO.getEmail();
        String phone = userRegisterReqDTO.getPhone();

        ExceptionUtil.throwIf(!password.equals(confirmPassword),
                ()-> new ClientException(UserErrorCode.CONFIRM_PASSWORD_ERROR));
        //用户名或邮箱是否存在
        Long count = userMapper.selectCount(Wrappers.<UserDO>lambdaQuery()
                .eq(UserDO::getUsername, username));
        ExceptionUtil.throwIf(count > 0,
                ()->new ClientException(UserErrorCode.USER_NAME_EXIST_ERROR));

        // 2. 写入 user 表
        UserDO user = new UserDO()
        .setUsername(username)
        .setEmail(email)
        .setPhone(userRegisterReqDTO.getPhone())
        .setPassword(userRegisterReqDTO.getPassword())
        .setStatus(1)
        .setIsDeleted(false)
        .setCreatedAt(new Date())
        .setUpdatedAt(new Date());
        userMapper.insert(user);

        // 3. 写入 user_detail 表
        UserDetailDO detail = new UserDetailDO();
        detail.setUserId(user.getId())
        .setCreatedAt(new Date())
        .setUpdatedAt(new Date());
        userDetailMapper.insert(detail);
        return new UserRegisterRespDTO(username, password,email,phone);
    }
}
