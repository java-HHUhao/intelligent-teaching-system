package cn.edu.hhu.its.user.service.service;

import cn.edu.hhu.its.user.service.model.dto.req.UserLoginReqDTO;
import cn.edu.hhu.its.user.service.model.dto.req.UserRegisterReqDTO;
import cn.edu.hhu.its.user.service.model.dto.resp.UserLoginRespDTO;
import cn.edu.hhu.its.user.service.model.dto.resp.UserRegisterRespDTO;

/**
 * 普通用户接口
 */
public interface UserService {
    /**
     * 用户登录入口
     * @param userLoginReqDTO 入参
     * @return 登录返回结果
     */
    UserLoginRespDTO login(UserLoginReqDTO userLoginReqDTO);

    UserRegisterRespDTO register(UserRegisterReqDTO userRegisterReqDTO);
}
