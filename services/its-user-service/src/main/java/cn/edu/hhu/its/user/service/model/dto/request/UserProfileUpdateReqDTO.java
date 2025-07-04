package cn.edu.hhu.its.user.service.model.dto.request;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import java.util.Date;

/**
 * 用户个人资料更新请求参数
 */
@Data
public class UserProfileUpdateReqDTO {
    /**
     * 性别（male/female/other）
     */
    private String gender;

    /**
     * 生日
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
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
