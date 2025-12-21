package com.ayaka.aiagent.agent;

import com.ayaka.aiagent.agent.model.AgentState;
import com.ayaka.aiagent.exception.ErrorCode;
import com.ayaka.aiagent.exception.ThrowUtils;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;

import java.util.ArrayList;
import java.util.List;

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
     * 步骤执行
     * @return
     */
    public abstract String step();

    public void cleanup(){
        log.info("清理资源，agent 状态：{}", agentState);
    }
}
