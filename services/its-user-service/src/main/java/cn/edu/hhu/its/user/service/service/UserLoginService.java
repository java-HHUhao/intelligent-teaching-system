package cn.edu.hhu.its.user.service.service;

import cn.edu.hhu.its.user.service.model.dto.request.UserLoginReqDTO;
import cn.edu.hhu.its.user.service.model.dto.request.UserRegisterReqDTO;
import cn.edu.hhu.its.user.service.model.dto.response.UserLoginRespDTO;
import cn.edu.hhu.its.user.service.model.dto.response.UserRegisterRespDTO;
import jakarta.servlet.http.HttpServletRequest;

/**
 * 普通用户接口
 */
public interface UserLoginService {
    /**
     * 用户登录入口
     * @param userLoginReqDTO 入参
     * @return 登录返回结果
     */
    UserLoginRespDTO login(UserLoginReqDTO userLoginReqDTO);

    UserRegisterRespDTO register(UserRegisterReqDTO userRegisterReqDTO);

    void logout(HttpServletRequest request);
}
