package com.ayaka.aiagent.agent.model;

/**
 * Agent 执行状态枚举
 */
public enum AgentState {
    /**
     * 空闲
     */
    IDLE,
    /**
     * 执行中
     */
    RUNNING,
    /**
     * 执行完成
     */
    FINISHED,
    /**
     * 执行错误
     */
    ERROR
}
