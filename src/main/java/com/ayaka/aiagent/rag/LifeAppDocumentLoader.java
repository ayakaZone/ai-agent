package com.ayaka.aiagent.rag;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.markdown.MarkdownDocumentReader;
import org.springframework.ai.reader.markdown.config.MarkdownDocumentReaderConfig;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class LifeAppDocumentLoader {

    private final ResourcePatternResolver resourcePatternResolver;

    public LifeAppDocumentLoader(ResourcePatternResolver resourcePatternResolver) {
        this.resourcePatternResolver = resourcePatternResolver;
    }

    /**
     * 从本地资源读取文档
     * @return
     */
    public List<Document> loadDocument(){
        // 保存文档的集合
        List<Document> documents = new ArrayList<>();
        try {
            // 读取所有文档
            Resource[] resources = resourcePatternResolver.getResources("classpath:document/*.md");
            // 遍历文档为文档加载器设置元信息 fileName
            for (Resource resource : resources) {
                String filename = resource.getFilename();
                // 加载配置器
                MarkdownDocumentReaderConfig readerConfig = MarkdownDocumentReaderConfig.builder()
                        .withHorizontalRuleCreateDocument(true)
                        .withIncludeCodeBlock(false)
                        .withIncludeBlockquote(false)
                        .withAdditionalMetadata("filename", filename)
                        .build();
                // 调用加载器读取文档
                MarkdownDocumentReader markdownDocumentReader = new MarkdownDocumentReader(resource, readerConfig);
                // 保存到文档集合
                documents.addAll(markdownDocumentReader.get());
            }
        } catch (IOException e) {
            log.error("读取文档失败，异常信息: ", e);
        }
        return documents;
    }
}
