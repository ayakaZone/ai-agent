
package com.ayaka.aiagent.advice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.advisor.api.AdvisedRequest;
import org.springframework.ai.chat.client.advisor.api.AdvisedResponse;
import org.springframework.ai.chat.client.advisor.api.CallAroundAdvisor;
import org.springframework.ai.chat.client.advisor.api.CallAroundAdvisorChain;
import org.springframework.ai.chat.client.advisor.api.StreamAroundAdvisor;
import org.springframework.ai.chat.client.advisor.api.StreamAroundAdvisorChain;
import org.springframework.ai.chat.model.MessageAggregator;
import reactor.core.publisher.Flux;

@Slf4j
public class MyLoggerAdvisor implements CallAroundAdvisor, StreamAroundAdvisor {
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
         log.info("before request: {}", request.userText());
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
