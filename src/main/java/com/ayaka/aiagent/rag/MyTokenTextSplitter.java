package com.ayaka.aiagent.rag;

import org.springframework.ai.document.Document;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MyTokenTextSplitter {

    /**
     * 基于 Token 文本分割文档
     * @param documentList
     * @return
     */
    public List<Document> splitDocument(List<Document> documentList){
        TokenTextSplitter splitter = new TokenTextSplitter();
        return splitter.split(documentList);
    }

    /**
     * 指定分割规则
     */
    public List<Document> splitCustomized(List<Document> documentList){
        TokenTextSplitter splitter = new TokenTextSplitter
                (200, 100,10, 5009, true);
        return splitter.split(documentList);
    }
}
