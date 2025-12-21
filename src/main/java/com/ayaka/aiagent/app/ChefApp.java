package com.ayaka.aiagent.app;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.InMemoryChatMemory;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.stereotype.Component;

import static org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor.CHAT_MEMORY_CONVERSATION_ID_KEY;
import static org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor.CHAT_MEMORY_RETRIEVE_SIZE_KEY;

@Component
@Slf4j
public class ChefApp {

    // 初始化 ChatClient
    private final ChatClient chatClient;

    /**
     * systemPrompt 系统提示词
     */
    private static final String SYSTEM_PROMPT =
            "你是一名传承中国八大菜系的专业主厨，拥有数十年中餐烹饪与教学经验。" +
                    "你的核心任务是以专业、细致、可操作的方式，分享中餐烹饪的经验、技巧与方法。" +
                    "你深谙川、鲁、粤、苏、浙、闽、湘、徽八大菜系的精髓，擅长将传统技法与现代厨房实践结合。" +
                    "你的语言风格亲切、专业且充满热情，如同一位师傅在厨房中亲自指导学员。" +
                    "现在用户是一位厨房新手，想问你各种厨房知识，请根据以上原则回答。";

    public ChefApp(ChatModel dashscopeChatModel) {
        // 历史对话存储
        ChatMemory chatMemory = new InMemoryChatMemory();
        chatClient = ChatClient.builder(dashscopeChatModel)
                .defaultSystem(SYSTEM_PROMPT)
                .defaultAdvisors(new MessageChatMemoryAdvisor(chatMemory))
                .build();
    }

    public String doChat(String message, String id) {
        ChatResponse response = chatClient
                .prompt()
                .user(message)
                .advisors(spec -> spec
                        .param(CHAT_MEMORY_CONVERSATION_ID_KEY, id)
                        .param(CHAT_MEMORY_RETRIEVE_SIZE_KEY, 10))
                .call()
                .chatResponse();
        String content = response.getResult().getOutput().getText();
        log.info("content：{}", content);
        return content;
    }
}
