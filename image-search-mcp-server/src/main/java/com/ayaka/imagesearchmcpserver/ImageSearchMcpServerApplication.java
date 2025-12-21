package com.ayaka.imagesearchmcpserver;

import com.ayaka.imagesearchmcpserver.tools.ImageSearchTool;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ImageSearchMcpServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ImageSearchMcpServerApplication.class, args);
    }


    /**
     * 注册工具
     * @param imageSearchTool
     * @return
     */
    @Bean
    public ToolCallbackProvider toolCallbackProvider(ImageSearchTool imageSearchTool) {
        return MethodToolCallbackProvider.builder().toolObjects(imageSearchTool).build();
    }
}
