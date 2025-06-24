package cn.edu.hhu.its.generator.service.mapper;

import cn.edu.hhu.its.generator.service.model.domain.GenerateTask;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author lianghao
* @description 针对表【generate_task(资源生成任务记录表)】的数据库操作Mapper
* @createDate 2025-06-23 15:33:57
* @Entity cn.edu.hhu.its.generator.service.model.domain.GenerateTask
*/
@Mapper
public interface GenerateTaskMapper extends BaseMapper<GenerateTask> {

}




