
package com.ayaka.aiagent.agent;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public abstract class ReActAgent extends BaseAgent{

    /**
     * 思考
     * @return
     */
    public abstract boolean think();

    /**
     * 根据思考结果行动
     * @return
     */
    public abstract String act();

    /**
     * 具体执行的单个步骤
     * @return
     */
    @Override
    public String step() {
        try {
            // 思考
            boolean thinkResult = think();
            if (!thinkResult) {
                return "思考完成 - 无需行动";
            }
            // 行动
            return act();
        } catch (Exception e) {
            e.printStackTrace();
            return "执行步骤错误：" + e.getMessage();
        }
    }
}
