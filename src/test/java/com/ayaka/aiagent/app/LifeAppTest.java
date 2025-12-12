package com.ayaka.aiagent.app;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

@Slf4j
@SpringBootTest
class LifeAppTest {

    @Resource
    private LifeApp lifeApp;

    @Test
    void testAnchorApp() {

        String chatId = UUID.randomUUID().toString();

        // 第一轮
        log.info("开始第一轮");
        String userPrompt = "你好,我是 ivv";
        String content = lifeApp.doChat(userPrompt, chatId);
        log.info("content: {}", content);
        // 第二轮
        log.info("开始第二轮");
        userPrompt = "我有一个正在交往的对象，她叫 yaa";
        content = lifeApp.doChat(userPrompt, chatId);
        log.info("content: {}", content);
        // 第三轮
        log.info("开始第三轮");
        userPrompt = "你还记得我之前和你说的我有一个对象吗，他叫什么";
        content = lifeApp.doChat(userPrompt, chatId);
        log.info("content: {}", content);
    }

    @Test
    void testDoChatWithReport() {

        String chatId = UUID.randomUUID().toString();

        // 第一轮
        log.info("开始第一轮");
        String userPrompt = "你好,我是 ivv";
        lifeApp.doChatWithReport(userPrompt, chatId);
    }

    @Test
    void doChatWithRag() {
        String chatId = UUID.randomUUID().toString();

        // 第一轮
        log.info("开始第一轮——————RAG");
        String userPrompt = "最近我总是睡不好，工作和生活也搞得一团糟，我该怎么办，有没有这方面的相关书籍或课程推荐";
        lifeApp.doChatWithRag(userPrompt, chatId);
    }
}