package com.ayaka.aiagent.rag;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.rag.Query;
import org.springframework.ai.rag.preretrieval.query.transformation.QueryTransformer;
import org.springframework.ai.rag.preretrieval.query.transformation.RewriteQueryTransformer;
import org.springframework.stereotype.Component;

@Component
public class QueryReader {

    private final QueryTransformer queryTransformer;

    public QueryReader(ChatModel dashscopeChatModel) {
        ChatClient.Builder builder = ChatClient.builder(dashscopeChatModel);
        queryTransformer = RewriteQueryTransformer.builder().chatClientBuilder(builder).build();

    }

    /**
     * 查询文本重写
     * @param query
     * @return
     */
    public String doQueryRewriter(String query) {
        return queryTransformer.transform(new Query(query)).text();
    }
}
