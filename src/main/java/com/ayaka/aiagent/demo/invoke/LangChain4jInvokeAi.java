package com.ayaka.aiagent.demo.invoke;

import dev.langchain4j.community.model.dashscope.QwenChatModel;
import org.springframework.stereotype.Component;

//@Component
public class LangChain4jInvokeAi {
    public static void main(String[] args) throws Exception {

        QwenChatModel qwenChatModel = QwenChatModel.builder().apiKey(ApiKey.API_KEY).modelName("qwen-max").build();
        String chat = qwenChatModel.chat("你好，我是鱼皮");
        System.out.println(chat);

    }
}

