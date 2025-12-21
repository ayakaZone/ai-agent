
package com.ayaka.aiagent.advice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.advisor.api.*;
import org.springframework.ai.chat.model.MessageAggregator;
import reactor.core.publisher.Flux;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public class ReReadingAdvisor implements CallAroundAdvisor, StreamAroundAdvisor {
    private int order;

    /**
     * 顾问名称
     * @return
     */
    public String getName() {
        return this.getClass().getSimpleName();
    }

    /**
     * 顾问执行顺序
     * @return
     */
    public int getOrder() {
        return this.order;
    }

    /**
     * 请求执行前的处理
     * @param request
     * @return
     */
    private AdvisedRequest before(AdvisedRequest request) {
        // PromptTemplate 模板
        HashMap<String, Object> advisedUserParams = new HashMap<>(request.userParams());
        advisedUserParams.put("re2_input-query", request.userText());
        // 替换请求中的用户Prompt
        AdvisedRequest.from(request).userText("""
                {re2_input-query} 
                Read the question again: {re2_input-query}
                """)
                .userParams(advisedUserParams)
                .build();
        return request;
    }

    /**
     * 响应执行后的处理
     * @param advisedResponse
     */
    private void observeAfter(AdvisedResponse advisedResponse) {
        log.info("observeAfter response: {}", advisedResponse.response().getResult().getOutput().getText());
    }

    /**
     * 环式处理：非流式
     * @param advisedRequest
     * @param chain
     * @return
     */
    public AdvisedResponse aroundCall(AdvisedRequest advisedRequest, CallAroundAdvisorChain chain) {
        advisedRequest = this.before(advisedRequest);
        AdvisedResponse advisedResponse = chain.nextAroundCall(advisedRequest);
        this.observeAfter(advisedResponse);
        return advisedResponse;
    }

    /**
     * 环式处理：流式（response）只能查看
     * @param advisedRequest
     * @param chain
     * @return
     */
    public Flux<AdvisedResponse> aroundStream(AdvisedRequest advisedRequest, StreamAroundAdvisorChain chain) {
        advisedRequest = this.before(advisedRequest);
        Flux<AdvisedResponse> advisedResponses = chain.nextAroundStream(advisedRequest);
        return (new MessageAggregator()).aggregateAdvisedResponse(advisedResponses, this::observeAfter);
    }
}
