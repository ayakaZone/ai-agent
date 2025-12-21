package com.ayaka.aiagent.tools;

import cn.hutool.core.io.FileUtil;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;

import static com.ayaka.aiagent.constant.FileConstant.FILE_PATH_DIR;
public class FileOperationTool {

    // 文件目录地址
    private final String FILE_DIR = FILE_PATH_DIR + "/files";


    /**
     * 读取文件
     * @param fileName
     * @return
     */
    @Tool(description = "Read Content from a file")
    public String readFiles(@ToolParam(description = "Name of the file to read") String fileName){
        // 文件名称
        String filePath = FILE_DIR + "/" + fileName;
        try {
            return FileUtil.readUtf8String(filePath);
        } catch (Exception e) {
            return "Error reading fail: " + e.getMessage();
        }
    }

    /**
     * 写入文件
     * @param fileName
     * @param content
     * @return
     */
    @Tool(description = "Write content to a file")
    public String writeFiles(@ToolParam(description = "Name of the file to write") String fileName,
                             @ToolParam(description = "Content to writer to the file") String content) {
        String filePath = FILE_DIR + "/" + fileName;
        try {
            // 创建目录
            FileUtil.mkdir(FILE_DIR);
            FileUtil.writeUtf8String(content, filePath);
            return "File written successfully to " + filePath;
        } catch (Exception e) {
            return "Error writing to file: " + e.getMessage();
        }
    }
}
