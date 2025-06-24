package cn.edu.hhu.its.generator.service.mapper;

import cn.edu.hhu.its.generator.service.model.domain.GenerateTaskLog;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author lianghao
* @description 针对表【generate_task_log(资源生成任务的状态变更与日志记录)】的数据库操作Mapper
* @createDate 2025-06-23 15:34:30
* @Entity cn.edu.hhu.its.generator.service.model.domain.GenerateTaskLog
*/
@Mapper
public interface GenerateTaskLogMapper extends BaseMapper<GenerateTaskLog> {

}




