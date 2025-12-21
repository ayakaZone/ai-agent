package com.ayaka.aiagent.chatmemory;


import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import org.objenesis.strategy.StdInstantiatorStrategy;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.messages.Message;

import java.io.*;
import java.util.ArrayList;
import java.util.List;


/**
 * 基于文件持久化的对话记忆
 */
public class FileBasedChatMemory implements ChatMemory {

    private final String BASE_DIR;

    // Kryo 序列化库
    private static final Kryo kryo = new Kryo();

    static {
        kryo.setRegistrationRequired(false);
        // 设置实例化策略
        kryo.setInstantiatorStrategy(new StdInstantiatorStrategy());
    }

    /**
     * 初始化文件目录
     * @param baseDir
     */
    public FileBasedChatMemory(String baseDir) {
        BASE_DIR = baseDir;
        File file = new File(baseDir);
        if (!file.exists()) {
            file.mkdirs();
        }
    }

    /**
     * 添加对话消息
     * @param conversationId
     * @param messages
     */
    public void add(String conversationId, List<Message> messages) {
        // 读取源文件
        List<Message> orCreateConversation = getOrCreateConversation(conversationId);
        // 更新对话消息
        orCreateConversation.addAll(messages);
        // 保存文件
        saveConversation(conversationId, orCreateConversation);
    }

    /**
     * 获取对话消息
     * @param conversationId
     * @param lastN
     * @return
     */
    public List<Message> get(String conversationId, int lastN) {
        List<Message> orCreateConversation = getOrCreateConversation(conversationId);
        return orCreateConversation
                .stream()
                .skip(Math.max(0, orCreateConversation.size() - lastN))
                .toList();
    }

    /**
     * 清空对话
     * @param conversationId
     */
    public void clear(String conversationId) {
        File file = getConversionFile(conversationId);
        if (file.exists()) {
            file.delete();
        }
    }

    /**
     * 从文件中读取对话转为 Message 对象
     * @param ConversationId
     * @return
     */
    public List<Message> getOrCreateConversation(String ConversationId){
        File file = getConversionFile(ConversationId);
        if (!file.exists()) {
            return new ArrayList<>();
        }
        List<Message> messages = new ArrayList<>();
        try (Input input = new Input(new FileInputStream(file))){
            if (file.exists()) {
                messages = kryo.readObject(input, ArrayList.class);
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        return messages ;
    }

    /**
     * 把Message 消息对话持久化到本地文件
     * @param messages
     */
    public void saveConversation(String ConversationId, List<Message> messages){
        File file = getConversionFile(ConversationId);
        try (Output output = new Output(new FileOutputStream(file))){
            kryo.writeObject(output, messages);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public File getConversionFile(String ConversationId){
        return new File(BASE_DIR, ConversationId + ".kryo");
    }
}

