package com.ayaka.aiagent.tools;

import cn.hutool.core.io.FileUtil;
import cn.hutool.http.HttpUtil;
import opennlp.tools.util.DownloadUtil;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;

import java.io.File;

import static com.ayaka.aiagent.constant.FileConstant.FILE_PATH_DIR;

public class ResourceDownloadTool {

    @Tool(description = "Download a resource from a given URL")
    public String downloadResource(@ToolParam(description = "URL of the resource to download") String url,
                                   @ToolParam(description = "Name of the file to save the download resource") String fileName){

        String fileDir = FILE_PATH_DIR + "/downloads";
        String filePath = fileDir + "/" + fileName;
        try {
            FileUtil.mkdir(fileDir);
            HttpUtil.downloadFile(url, new File(filePath));
            return "File downloaded successfully to " + filePath;
        } catch (Exception e) {
            return "Error downloading resource: " + e.getMessage();
        }
    }
}








