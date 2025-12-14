package com.ayaka.aiagent.app;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
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
        String userPrompt = "生活烦恼";
        lifeApp.doChatWithRag(userPrompt, chatId);
    }

    @Test
    void doChatWithTools() {
        // 测试联网搜索问题的答案
        testMessage("周末想带女朋友去上海约会，推荐几个适合情侣的小众打卡地？");

        // 测试网页抓取：恋爱案例分析
        testMessage("最近和对象吵架了，看看编程导航网站（codefather.cn）的其他情侣是怎么解决矛盾的？");

        // 测试资源下载：图片下载
        testMessage("直接下载一张适合做手机壁纸的星空情侣图片为文件");

        // 测试终端操作：执行代码
        testMessage("执行 Python3 脚本来生成数据分析报告");

        // 测试文件操作：保存用户档案
        testMessage("保存我的生活状况文件");

        // 测试 PDF 生成
        testMessage("生成一份‘21天养成良好生活习惯’的PDF，包含每日计划，行程安排");
    }

    private void testMessage(String message) {
        String tips = "请你使用我提供给你的工具完成";
        String chatId = UUID.randomUUID().toString();
        String answer = lifeApp.doChatWithTools(message, chatId);
        Assertions.assertNotNull(answer);
    }
}