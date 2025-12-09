package com.ayaka.aiagent.app;

import jakarta.annotation.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;


@SpringBootTest
class ChefAppTest {

    @Resource
    private ChefApp chefApp;

    @Test
    void testChat() {
        String chatId = UUID.randomUUID().toString();
        // 第一轮
        System.out.println("第一轮");
        String message = "你好，我是程序员鱼皮";
        String answer = chefApp.doChat(message, chatId);
        Assertions.assertNotNull(answer);
        // 第二轮
        System.out.println("第二轮");
        message = "我想让另一半（编程导航）更爱我";
        answer = chefApp.doChat(message, chatId);
        Assertions.assertNotNull(answer);
        // 第三轮
        System.out.println("第三轮");
        message = "我的另一半叫什么来着？刚跟你说过，帮我回忆一下";
        answer = chefApp.doChat(message, chatId);
        Assertions.assertNotNull(answer);
    }
}