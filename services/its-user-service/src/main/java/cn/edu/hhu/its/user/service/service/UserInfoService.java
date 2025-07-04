package cn.edu.hhu.its.user.service.service;

import cn.edu.hhu.its.user.service.model.dto.request.UserAccountUpdateReqDTO;
import cn.edu.hhu.its.user.service.model.dto.request.UserProfileUpdateReqDTO;
import cn.edu.hhu.its.user.service.model.dto.request.UsernameUpdateReqDTO;
import cn.edu.hhu.its.user.service.model.dto.response.UserAccountUpdateRespDTO;
import cn.edu.hhu.its.user.service.model.dto.response.UserProfileRespDTO;
import cn.edu.hhu.its.user.service.model.dto.response.UserProfileUpdateRespDTO;
import cn.edu.hhu.its.user.service.model.dto.response.UsernameUpdateRespDTO;

/**
 * 用户信息服务接口
 */
public interface UserInfoService {
    /**
     * 获取用户个人资料
     * @param userId 用户ID
     * @return 用户个人资料
     */
    UserProfileRespDTO getUserProfile(Long userId);

    /**
     * 更新用户个人资料
     * @param userId 用户ID
     * @param updateReq 更新请求
     * @return 更新后的用户资料
     */
    UserProfileUpdateRespDTO updateUserProfile(Long userId, UserProfileUpdateReqDTO updateReq);

    /**
     * 更新用户账户信息
     * @param userId 用户ID
     * @param updateReq 更新请求
     * @return 更新后的账户信息
     */
    UserAccountUpdateRespDTO updateUserAccount(Long userId, UserAccountUpdateReqDTO updateReq);

    /**
     * 修改用户名
     *
     * @param userId 用户ID
     * @param updateReq 修改用户名请求
     * @return 修改结果
     */
    UsernameUpdateRespDTO updateUsername(Long userId, UsernameUpdateReqDTO updateReq);
}
