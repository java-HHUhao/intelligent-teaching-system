package cn.edu.hhu.its.generator.service.services.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.edu.hhu.its.generator.service.model.domain.GenerateTask;
import cn.edu.hhu.its.generator.service.services.GenerateTaskService;
import cn.edu.hhu.its.generator.service.mapper.GenerateTaskMapper;
import org.springframework.stereotype.Service;

/**
* @author lianghao
* @description 针对表【generate_task(资源生成任务记录表)】的数据库操作Service实现
* @createDate 2025-06-23 15:33:57
*/
@Service
public class GenerateTaskServiceImpl extends ServiceImpl<GenerateTaskMapper, GenerateTask>
    implements GenerateTaskService{

}




