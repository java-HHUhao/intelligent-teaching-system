package cn.edu.hhu.spring.boot.starter.aiagent.tools;

import cn.edu.hhu.spring.boot.starter.aiagent.constant.FileConstant;
import cn.edu.hhu.spring.boot.starter.common.utils.ExceptionUtil;
import cn.hutool.core.io.FileUtil;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;

public class FileOperationTool {
    private final String FILE_DIR= FileConstant.FILE_SAVE_DIR+"\\file";

    @Tool(description = "读取文件内容")
    public String readFile(@ToolParam(description = "Name of the file to read")String fileName) {
        String filePath= FILE_DIR+"\\"+fileName;
        return ExceptionUtil.catchOrDefault(() -> FileUtil.readUtf8String(filePath), "文件不存在");
    }

    @Tool(description = "写入文件")
    public String writeFile(
            @ToolParam(description = "Name of the file to write") String fileName,
            @ToolParam(description = "Content to write to the file") String content){
        String filePath= FILE_DIR+"\\"+fileName;
        try {
            // 创建目录
            FileUtil.mkdir(FILE_DIR);
            FileUtil.writeUtf8String(content, filePath);
            return "File written successfully to: " + filePath;
        } catch (Exception e) {
            return "Error writing to file: " + e.getMessage();
        }
    }
}