package com.ayaka.aiagent.app;

import com.ayaka.aiagent.advice.MyLoggerAdvisor;
import com.ayaka.aiagent.advice.ReReadingAdvisor;
import com.ayaka.aiagent.chatmemory.FileBasedChatMemory;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.QuestionAnswerAdvisor;
import org.springframework.ai.chat.client.advisor.api.Advisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor.CHAT_MEMORY_CONVERSATION_ID_KEY;
import static org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor.CHAT_MEMORY_RETRIEVE_SIZE_KEY;

@Component
@Slf4j
public class LifeApp {

    private final ChatClient chatClient;

    private final String SYSTEM_PROMPT =
            "你是“AI生活对谈大师”，一位融合了心理咨询师、人生导师、故事讲述者和智慧朋友多重角色的生活导师。" +
                    "你拥有心理学、社会学、哲学等多领域知识，擅长通过深度对话帮助用户解决生活、情感、职场、人际关系等问题。" +
                    "我需要你把回复字数控制在300字以内，节约成本" +
                    "现在，请用你温暖而智慧的方式开始与用户对话。";

    public LifeApp(ChatModel dashscopeChatModel) {
//        ChatMemory chatMemory = new InMemoryChatMemory();
        String memoryDir = System.getProperty("user.dir") + "/chat-memory";
        ChatMemory chatMemory = new FileBasedChatMemory(memoryDir);
        chatClient = ChatClient
                .builder(dashscopeChatModel)
                .defaultSystem(SYSTEM_PROMPT)
                .defaultAdvisors(
                        // 对话记忆顾问
                        new MessageChatMemoryAdvisor(chatMemory),
                        // 自定义日志顾问
                        new MyLoggerAdvisor(),
                        // 自定义重读顾问
                        new ReReadingAdvisor()
                ).build();
    }

    /**
     * 记忆对话
     * @param message
     * @param chatId
     * @return
     */
    public String doChat(String message, String chatId) {
        ChatResponse chatResponse = chatClient.prompt()
                .user(message)
                .advisors(spec -> {
                    // 添加对话记忆参数
                    spec.param(CHAT_MEMORY_CONVERSATION_ID_KEY, chatId)
                            .param(CHAT_MEMORY_RETRIEVE_SIZE_KEY, 10);
                })
                .call()
                .chatResponse();
        if (chatResponse == null) {
            throw new RuntimeException();
        }
        return chatResponse.getResult().getOutput().getText();
    }


    // java 14新特性 record，快速构建一个类
    record LifeReport(String userName, List<String> adviceList) {
    }

    /**
     * 结构化输出
     * @param message
     * @param chatId
     * @return
     */
    public LifeReport doChatWithReport(String message, String chatId) {

        LifeReport lifeReport = chatClient.prompt()
                // 覆盖系统默认提示词，我们为他添加结构化输出的提示
                .system(SYSTEM_PROMPT + "每次对话结束后都要生成一份报告，标题为{用户名}的恋爱报告,内容为建议列表")
                .user(message)
                .advisors(spec -> {
                    // 添加对话记忆参数
                    spec.param(CHAT_MEMORY_CONVERSATION_ID_KEY, chatId)
                            .param(CHAT_MEMORY_RETRIEVE_SIZE_KEY, 10);
                })
                .call()
                // BeanOutputConverter 转换器，转化为我们指定的类型
                .entity(LifeReport.class);
        if (lifeReport == null) {
            throw new RuntimeException();
        }
        log.info("anchorReport: {}", lifeReport);
        return lifeReport;
    }

    @Resource
    private VectorStore vectorStore;
    @Resource
    private Advisor lifeAppRagCloundAdvisor;
    public String doChatWithRag(String message, String chatId){
        ChatResponse chatResponse = chatClient.prompt()
                .user(message)
                .advisors(spec -> {
                    // 添加对话记忆参数
                    spec.param(CHAT_MEMORY_CONVERSATION_ID_KEY, chatId)
                            .param(CHAT_MEMORY_RETRIEVE_SIZE_KEY, 10);
                })
                // 问答顾问
                // .advisors(new QuestionAnswerAdvisor(vectorStore))
                // 增强检索顾问（云端知识库）
                .advisors(lifeAppRagCloundAdvisor)
                .call()
                .chatResponse();
        if (chatResponse == null) {
            throw new RuntimeException();
        }
        String content = chatResponse.getResult().getOutput().getText();
        log.info("content: {}", content);
        return content;
    }
}
