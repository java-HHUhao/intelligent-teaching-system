package cn.edu.hhu.its.aiagent.service.outputmode;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class PromptPropertiesDTO {
    private String prompt;
    private String chatId;
    private int memorySize;
}
