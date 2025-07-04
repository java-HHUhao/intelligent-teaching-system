package cn.edu.hhu.its.user.service.model.dto.response;

import lombok.Data;
import lombok.experimental.Accessors;
import java.util.Date;

/**
 * 用户个人资料更新响应参数
 */
@Data
@Accessors(chain = true)
public class UserProfileUpdateRespDTO {
    /**
     * 性别
     */
    private String gender;

    /**
     * 生日
     */
    private Date birthday;

    /**
     * 所在地区
     */
    private String address;

    /**
     * 个人简介
     */
    private String bio;
}
