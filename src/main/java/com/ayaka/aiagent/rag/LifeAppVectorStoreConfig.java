package com.ayaka.aiagent.rag;

import jakarta.annotation.Resource;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class LifeAppVectorStoreConfig {

    @Resource
    private LifeAppDocumentLoader lifeAppDocumentLoader;

    /**
     * 向量数据库配置类
     * @param dashscopeEmbeddingModel
     * @return
     */
    @Bean
    SimpleVectorStore lifeAppVectorStore(EmbeddingModel dashscopeEmbeddingModel) {
        // 读取文档
        List<Document> documents = lifeAppDocumentLoader.loadDocument();
        // 创建向量存储数据库
        SimpleVectorStore simpleVectorStore = SimpleVectorStore.builder(dashscopeEmbeddingModel).build();
        // 保存
        simpleVectorStore.add(documents);
        return simpleVectorStore;
    }
}
