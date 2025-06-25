package cn.edu.hhu.its.user.service.controller;

import cn.edu.hhu.spring.boot.starter.distributedid.core.serviceid.snowflake.SnowflakeIdInfo;
import cn.edu.hhu.spring.boot.starter.distributedid.handler.IdGeneratorManager;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserLoginController {

    @PostMapping("/test")
    public String test(){
        long id= IdGeneratorManager.getDefaultServiceIdGenerator().nextId(1L);
        SnowflakeIdInfo snowflakeIdInfo=IdGeneratorManager.getDefaultServiceIdGenerator().parseSnowflakeId(id);
        System.out.println(id);
        System.out.println(snowflakeIdInfo);
        return String.valueOf(id);
    }
}
