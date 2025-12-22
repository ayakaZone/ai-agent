package com.ayaka.aiagent.agent;

import cn.hutool.core.collection.CollUtil;
import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatOptions;
import com.ayaka.aiagent.agent.model.AgentState;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.ToolResponseMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.model.tool.ToolCallingManager;
import org.springframework.ai.model.tool.ToolExecutionResult;
import org.springframework.ai.tool.ToolCallback;

import java.util.List;
import java.util.stream.Collectors;

@EqualsAndHashCode(callSuper = true)
@Data
@Slf4j
public class ToolCallAgent extends ReActAgent {

    /**
     * 所有的工具
     */
    private final ToolCallback[] availableTools;

    /**
     * 包含需要调用的工具的响应结果
     */
    private ChatResponse toolCallChatResponse;

    /**
     * 工具调用管理者
     */
    private final ToolCallingManager toolCallingManager;

    private final ChatOptions chatOptions;

    public ToolCallAgent(ToolCallback[] availableTools) {
        super();
        this.availableTools = availableTools;
        this.toolCallingManager = ToolCallingManager.builder().build();
        this.chatOptions = DashScopeChatOptions.builder()
                .withProxyToolCalls(true) // 禁用 Spring AI 自动调用工具
                .build();
    }

    /**
     * 具体思考过程
     *
     * @return
     */
    @Override
    public boolean think() {
        // 如果下一个步骤提示词不为空，就添加到会话记忆中
        if (getNextStepPrompt() != null && !getNextStepPrompt().isEmpty()) {
            UserMessage userMessage = new UserMessage(getNextStepPrompt());
            getMessageList().add(userMessage);
        }
        List<Message> messageList = getMessageList();
        Prompt prompt = new Prompt(messageList, chatOptions);
        try {
            // 调用 AI 获得响应结果
            this.toolCallChatResponse = getChatClient()
                    .prompt(prompt)
                    .system(getSystemPrompt())
                    .tools(availableTools)
                    .call()
                    .chatResponse();
            // 获取助手消息
            AssistantMessage assistantMessage = toolCallChatResponse.getResult().getOutput();
            // 获取 AI 需要调用的工具集合
            List<AssistantMessage.ToolCall> toolCallList = assistantMessage.getToolCalls();
            // AI 的响应文本
            String result = assistantMessage.getText();
            // 保存助手消息
            setChatResponse(result);
            // 打印日志
            log.info(getAgentName() + "的思考: " + result);
            log.info(getAgentName() + "需要调用: " + toolCallList.size() + " 个工具使用");
            // 需要调用的工具
            String toolCallInfo = toolCallList.stream()
                    .map(toolCall -> String.format("工具名称: %s, 调用参数: %s",
                            toolCall.name(), toolCall.arguments())
                    )
                    .collect(Collectors.joining("\n"));
            log.info(getAgentName() + "需要调用的工具: \n" + toolCallInfo);
            // 判断是否需要手动记录助手消息
            if (toolCallList.isEmpty()) {
                // 手动记录并结束思考
                messageList.add(assistantMessage);
                return false;
            } else {
                // 自动记录,并调用工具
                return true;
            }
        } catch (Exception e) {
            log.error(getAgentName() + " 的思考过程出现异常：" + e.getMessage());
            messageList.add(new AssistantMessage("思考时遇到错误: " + e.getMessage()));
            return false;
        }
    }

    /**
     * 具体执行过程
     *
     * @return
     */
    @Override
    public String act() {
        // 校验
        if (!toolCallChatResponse.hasToolCalls()) {
            return "无需工具调用";
        }
        // 调用工具
        Prompt prompt = new Prompt(getMessageList(), chatOptions);
        ToolExecutionResult toolExecutionResult = toolCallingManager.executeToolCalls(prompt, toolCallChatResponse);
        // 记录消息上下文
        setMessageList(toolExecutionResult.conversationHistory());
        // 获得工具调用的结果
        ToolResponseMessage toolResponseMessage =
                (ToolResponseMessage) CollUtil.getLast(toolExecutionResult.conversationHistory());
        // 改写结果描述形式
        String result = toolResponseMessage.getResponses()
                .stream()
                .map(response -> "使用了" + response.name() + "工具工作,结果: " + response.responseData())
                .collect(Collectors.joining("\n"));
        // 判断是否包含了终止执行的工具
        boolean terminateToolCalled = toolResponseMessage.getResponses()
                .stream()
                .anyMatch(response -> "doTerminate".equals(response.name()));
        // 修改状态结束执行
        if (terminateToolCalled) {
            setAgentState(AgentState.FINISHED);
        }
        log.info(getAgentName() + "的输出: " + result);
        return result;
    }
}
