package com.ayaka.aiagent.rag;

import jakarta.annotation.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PgVectorVectorStoreConfigTest {

    @Resource
    VectorStore vectorStore;
    @Test
    void pgVectorVectorStore() {
/*        List<Document> documents = List.of(
                new Document("生活烦恼怎么解决? 问 AI 生活对谈导师啊，看书学课程啊", Map.of("meta1", "meta1")),
                new Document("社交来往问题怎么解决? 问 AI 生活对谈导师啊，多去外面交朋友啊", Map.of("meta1", "meta1")),
                new Document("人生没有方向怎么解决? 问 AI 生活对谈导师啊，去找一些导师聊一聊啊", Map.of("meta1", "meta1")));

        // Add the documents to PGVector
        vectorStore.add(documents);*/

        // Retrieve documents similar to a query
        List<Document> results = this.vectorStore.similaritySearch(SearchRequest.builder().query("社交来往的问题我该怎么办").topK(5).build());
        Assertions.assertNotNull(results);
    }
}

