package cn.edu.hhu.its.generator.service.services.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.edu.hhu.its.generator.service.model.domain.GenerateTaskLog;
import cn.edu.hhu.its.generator.service.services.GenerateTaskLogService;
import cn.edu.hhu.its.generator.service.mapper.GenerateTaskLogMapper;
import org.springframework.stereotype.Service;

/**
* @author lianghao
* @description 针对表【generate_task_log(资源生成任务的状态变更与日志记录)】的数据库操作Service实现
* @createDate 2025-06-23 15:34:30
*/
@Service
public class GenerateTaskLogServiceImpl extends ServiceImpl<GenerateTaskLogMapper, GenerateTaskLog>
    implements GenerateTaskLogService{

}




