package com.ayaka.aiagent.controller;

import com.ayaka.aiagent.agent.Manus;
import com.ayaka.aiagent.app.LifeApp;
import jakarta.annotation.Resource;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import reactor.core.publisher.Flux;

import java.awt.*;
import java.io.IOException;

@RestController
@RequestMapping("/ai")
public class AiController {

    @Resource
    private LifeApp lifeApp;

    @Resource
    private ToolCallback[] allTools;

    @Resource
    private ChatModel dashscopeChatModel;

    /**
     * 大模型 AI 对话同步模式
     *
     * @param message
     * @param chatId
     * @return
     */
    @GetMapping("/life_app/chat/sync")
    public String doChatWithLifeAppSync(String message, String chatId) {
        return lifeApp.doChat(message, chatId);
    }

    /**
     * 大模型 AI 对话SSE流式模式
     *
     * @param message
     * @param chatId
     * @return
     */
    @GetMapping(value = "/life_app/chat/sse", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> doChatWithLifeAppSSE(String message, String chatId) {
        return lifeApp.doChatByStream(message, chatId);
    }

    /**
     * 大模型 AI 对话SSE流式模式
     *
     * @param message
     * @param chatId
     * @return
     */
    @GetMapping(value = "/life_app/chat/server_sent_event")
    public Flux<ServerSentEvent<String>> doChatWithLifeAppSSEServerSentEvent(String message, String chatId) {
        return lifeApp.doChatByStream(message, chatId)
                .map(chunk -> ServerSentEvent.<String>builder()
                        .data(chunk)
                        .build());
    }

    /**
     * 大模型 AI 对话 SSEEmitter
     *
     * @param message
     * @param chatId
     * @return
     */
    @GetMapping(value = "/life_app/chat/sse_emitter")
    public SseEmitter doChatWithLifeAppSSEEmitter(String message, String chatId) {
        // 创建 SSEEMitter 对象,并设置超时时间
        SseEmitter sseEmitter = new SseEmitter(180000L);
        // 获取 Flux 数据流并开启订阅
        lifeApp.doChatByStream(message, chatId)
                // 订阅数据流
                .subscribe(chunk -> {
                            try {
                                // 发送数据碎片
                                sseEmitter.send(chunk);
                            } catch (IOException e) {
                                sseEmitter.completeWithError(e);
                            }
                        },      // 处理错误时结束订阅
                        sseEmitter::completeWithError,
                        // 正常结束订阅
                        sseEmitter::complete);
        return sseEmitter;
    }

    /**
     * Manus 智能体对话
     * @param message
     * @return
     */
    @GetMapping("/manus/chat")
    public SseEmitter doChatWithManus(String message) {
        Manus manus = new Manus(allTools, dashscopeChatModel);
        return manus.runStream(message);
    }
}





