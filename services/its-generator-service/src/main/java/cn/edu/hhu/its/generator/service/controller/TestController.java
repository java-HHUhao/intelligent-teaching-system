package cn.edu.hhu.its.generator.service.controller;

import cn.edu.hhu.its.generator.service.mapper.GenerateTaskLogMapper;
import cn.edu.hhu.its.generator.service.model.domain.GenerateTaskLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
    @Autowired
    GenerateTaskLogMapper generateTaskLogMapper;
    @GetMapping("/test")
    public String test(){
        GenerateTaskLog generateTaskLog = new GenerateTaskLog();
        generateTaskLog.setId(1L);
        generateTaskLogMapper.insert(generateTaskLog);
        return "success";
    }
}
