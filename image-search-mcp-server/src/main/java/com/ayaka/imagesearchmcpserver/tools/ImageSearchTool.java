package com.ayaka.imagesearchmcpserver.tools;

import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;


import org.springframework.ai.tool.annotation.Tool;

/**
 * 图片搜索工具
 */
@Component
public class ImageSearchTool {

    private String apiKey = "pcdpRnyP8K2Ibh0FYnLv10VzeqISKWTCGdflQypyTZyQQ2XMbnrHzCht";

    private static final String API_URL = "https://api.pexels.com/v1/search";

    @Tool(description = "Search for images in Pexels stock photo library")
    public String search(@ToolParam(description = "Search keywords") String query) {
        try {
            String body = HttpRequest.get(API_URL)
                    .header("Authorization", apiKey)
                    .form("query", query)
                    .form("per_page", "5")
                    .execute()
                    .body();

            JSONArray photos = JSONUtil.parseObj(body).getJSONArray("photos");
            List<String> urls = new ArrayList<>();

            for (int i = 0; i < photos.size(); i++) {
                JSONObject photo = photos.getJSONObject(i);
                urls.add(photo.getStr("url"));
            }

            return String.join(",", urls);
        } catch (Exception e) {
            return "搜索失败：" + e.getMessage();
        }
    }
}