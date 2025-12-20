package com.ayaka.aiagent.tools;

import org.springframework.ai.tool.annotation.Tool;


public class TerminateTool {

    /**
     * 终止执行工具
     */
    @Tool(description = """  
            Terminate the interaction when the request is met OR if the assistant cannot proceed further with the task.  
            "When you have finished all the tasks, call this tool to end the work.  
            """)
    public String doTerminate(){
        return "任务结束";
    }
}
