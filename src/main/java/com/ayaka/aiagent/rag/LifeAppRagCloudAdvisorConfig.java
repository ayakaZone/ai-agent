package com.ayaka.aiagent.rag;

import com.alibaba.cloud.ai.dashscope.api.DashScopeApi;
import com.alibaba.cloud.ai.dashscope.rag.DashScopeDocumentRetriever;
import com.alibaba.cloud.ai.dashscope.rag.DashScopeDocumentRetrieverOptions;
import org.springframework.ai.chat.client.advisor.RetrievalAugmentationAdvisor;
import org.springframework.ai.chat.client.advisor.api.Advisor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LifeAppRagCloudAdvisorConfig {

    @Value("${spring.ai.dashscope.api-key}")
    private String apiKey;

    /**
     * 云端知识库增强检索顾问
     * @return
     */
    @Bean
    Advisor lifeAppRagCloudAdvisor() {
        DashScopeApi dashScopeApi = new DashScopeApi(apiKey);
        final String documentName = "生活对谈知识库";
        DashScopeDocumentRetriever dashScopeDocumentRetriever = new DashScopeDocumentRetriever(
                dashScopeApi,
                DashScopeDocumentRetrieverOptions
                .builder()
                .withIndexName(documentName)
                .build());
        return RetrievalAugmentationAdvisor
                .builder()
                .documentRetriever(dashScopeDocumentRetriever)
                .build();
    }
}
