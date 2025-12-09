package com.ayaka.aiagent.demo.invoke;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
public class HttpInvokeAi {

    public static void main(String[] args) {
        String apiKey = ApiKey.API_KEY; // 或直接写死
        String url = "https://dashscope.aliyuncs.com/api/v1/services/aigc/text-generation/generation";

        // 构造 messages 数组
        JSONArray messages = new JSONArray();
        messages.add(new JSONObject()
                .set("role", "system")
                .set("content", "You are a helpful assistant."));
        messages.add(new JSONObject()
                .set("role", "user")
                .set("content", "你是谁？"));

        // 构造请求体
        JSONObject body = new JSONObject()
                .set("model", "qwen-plus")
                .set("input", new JSONObject().set("messages", messages))
                .set("parameters", new JSONObject().set("result_format", "message"));

        // 发送 POST
        String resp = HttpRequest.post(url)
                .header("Authorization", "Bearer " + apiKey)
                .header("Content-Type", "application/json")
                .body(body.toString())
                .execute()
                .body();

        System.out.println(resp);
    }

}
