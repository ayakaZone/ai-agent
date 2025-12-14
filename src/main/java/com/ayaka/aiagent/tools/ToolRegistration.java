package com.ayaka.aiagent.tools;

import org.springframework.ai.tool.ToolCallback;
import org.springframework.ai.tool.ToolCallbacks;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ToolRegistration {

    @Value("${search-api.api-key}")
    private String searchApiKey;

    @Bean
    public ToolCallback[] allTools() {
        // 统一注册
        PDFGenerationTool pdfGenerationTool = new PDFGenerationTool();
        ResourceDownloadTool resourceDownloadTool = new ResourceDownloadTool();
        TerminalOperationTool terminalOperationTool = new TerminalOperationTool();
        ScrapeWebPageTool scrapeWebPageTool = new ScrapeWebPageTool();
        FileOperationTool fileOperationTool = new FileOperationTool();
        WebSearchTool webSearchTool = new WebSearchTool(searchApiKey);
        // 封装到 ToolCallbacks 数组中
        return ToolCallbacks.from(
                pdfGenerationTool,
                resourceDownloadTool,
                terminalOperationTool,
                scrapeWebPageTool,
                fileOperationTool,
                webSearchTool);
    }
}
