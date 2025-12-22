package com.ayaka.aiagent.agent;

import com.ayaka.aiagent.agent.model.AgentState;
import com.ayaka.aiagent.exception.ErrorCode;
import com.ayaka.aiagent.exception.ThrowUtils;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * 智能体基础层，定义基本属性和执行步骤
 */
@Data
@Slf4j
public abstract class BaseAgent {

    /**
     * 核心属性
     */
    private String agentName;

    /**
     * 提示词
     */
    private String systemPrompt;
    private String nextStepPrompt;

    /**
     * 状态
     */
    private AgentState agentState = AgentState.IDLE;

    /**
     * 执行控制
     */
    private int maxSteps = 10;
    private int currentStep = 0;

    /**
     * LLM AI 模型
     */
    private ChatClient chatClient;

    /**
     * 自定义上下文记忆存储(Memory)
     */
    private List<Message> messageList = new ArrayList<>();

    /**
     * 保存生成的结果
     */
    private String chatResponse;

    /**
     * 运行代理
     *
     * @param userPrompt
     * @return
     */
    public String run(String userPrompt) {
        // 校验
        ThrowUtils.throwIf(agentState != AgentState.IDLE, ErrorCode.OPERATION_ERROR, "当前代理正在运行中");
        ThrowUtils.throwIf(userPrompt == null, ErrorCode.PARAM_ERROR, "用户提示词不能为空");
        // 更改状态
        agentState = AgentState.RUNNING;
        log.info("开始执行代理，agent 名称：{}", agentName);
        // 记录消息上下文
        messageList.add(new UserMessage(userPrompt));
        // 执行结果列表
        ArrayList<String> resultList = new ArrayList<>();
        try {
            // Loop 执行循环步骤
            for (int i = 0; i < maxSteps && agentState != AgentState.FINISHED; i++) {
                // 当前执行步骤次数
                currentStep = i + 1;
                log.info("执行步骤：【{}/{}】，agent 状态：{}", currentStep, maxSteps, agentState);
                // 执行步骤
                String stepResult = step();
                // 拼接到提示词中
                String result = "Step " + currentStep + ": " + stepResult;
                // 保存到结果列表中
                resultList.add(result);
            }
            // 是否超出步骤限制
            if (currentStep >= maxSteps) {
                // 修改状态
                agentState = AgentState.FINISHED;
                // 在结果中添加终止运行和达到执行次数上限提示
                resultList.add("Terminated: Reached Max Step(" + currentStep + ")");
            }
            return String.join("\n", resultList);
        } catch (Exception e) {
            agentState = AgentState.ERROR;
            log.error("代理执行异常", e);
            return "执行错误: " + e.getMessage();
        }
    }

    /**
     * 异步运行代理（SSEEmitter）
     *
     * @param userPrompt
     * @return
     */
    public SseEmitter runStream(String userPrompt) {
        // 创建 SSEEmitter 对象,并设置超时时间
        SseEmitter emitter = new SseEmitter(300000L);
        // 使用线程异步处理，避免阻塞线程
        CompletableFuture.runAsync(() -> {
            try {
                // 校验
                if (agentState != AgentState.IDLE) {
                    emitter.send("当前代理正在运行中: " + this.agentState);
                    // 结束数据流
                    emitter.complete();
                    return;
                }
                if (userPrompt == null) {
                    emitter.send("用户提示词不能为空");
                    // 结束数据流
                    emitter.complete();
                    return;
                }
                // 更改状态
                agentState = AgentState.RUNNING;
                log.info("开始执行代理，agent 名称：{}", agentName);
                // 记录消息上下文
                messageList.add(new UserMessage(userPrompt));

                try {
                    // Loop 执行循环步骤
                    for (int i = 0; i < maxSteps && agentState != AgentState.FINISHED; i++) {
                        // 当前执行步骤次数
                        currentStep = i + 1;
                        log.info("执行步骤：【{}/{}】，agent 状态：{}", currentStep, maxSteps, agentState);
                        // 执行步骤
                        String stepResult = step();
                        // 拼接到提示词中
                        String result = "Step " + currentStep + ": " + stepResult;
                        // 通过 emitter 发送工具调用结果
                        emitter.send(result);
                    }
                    // 是否超出步骤限制
                    if (currentStep >= maxSteps) {
                        // 修改状态
                        agentState = AgentState.FINISHED;
                        // 在结果中添加终止运行和达到执行次数上限提示
                        emitter.send("Terminated: Reached Max Step(" + currentStep + ")");
                    }
                    // 输出 AI 的生成结果
                    emitter.send(chatResponse);
                    // 正常结束, 关闭 emitter
                    emitter.complete();
                } catch (Exception e) {
                    // 修改执行状态
                    agentState = AgentState.ERROR;
                    log.error("代理执行异常", e);
                    // 发送异常信息
                    emitter.send("执行错误: " + e.getMessage());
                    // 停止数据流
                    emitter.complete();
                } finally {
                    // 清理资源
                    cleanup();
                }
            } catch (IOException e) {
                emitter.completeWithError(e);
            }
        });
        // 设置连接超时与成功回调
        emitter.onTimeout(() -> {
            this.agentState = AgentState.ERROR;
            this.cleanup();
            log.warn("SSE connection timed out");
        });

        emitter.onCompletion(() -> {
            if (agentState == AgentState.RUNNING) {
                this.agentState = AgentState.FINISHED;
            }
            this.cleanup();
            log.info("SSE connection completed");
        });

        return emitter;
    }

    /**
     * 步骤执行
     *
     * @return
     */
    public abstract String step();

    public void cleanup() {
        log.info("清理资源，agent 状态：{}", agentState);
    }
}
