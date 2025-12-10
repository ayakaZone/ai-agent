package com.ayaka.aiagent.app;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

@Slf4j
@SpringBootTest
class AnchorAppTest {

    @Resource
    private AnchorApp anchorApp;

    @Test
    void testAnchorApp() {

        String chatId = UUID.randomUUID().toString();

        // 第一轮
        log.info("开始第一轮");
        String userPrompt = "你好,我是 ivv";
        String content = anchorApp.doChat(userPrompt, chatId);
        log.info("content: {}", content);
        // 第二轮
        log.info("开始第二轮");
        userPrompt = "我有一个正在交往的对象，她叫 yaa";
        content = anchorApp.doChat(userPrompt, chatId);
        log.info("content: {}", content);
        // 第三轮
        log.info("开始第三轮");
        userPrompt = "你还记得我之前和你说的我有一个对象吗，他叫什么";
        content = anchorApp.doChat(userPrompt, chatId);
        log.info("content: {}", content);
    }
}