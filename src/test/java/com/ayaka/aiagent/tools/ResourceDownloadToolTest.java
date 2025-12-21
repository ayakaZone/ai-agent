package com.ayaka.aiagent.tools;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ResourceDownloadToolTest {

    @Test
    void downloadResource() {

        String resourceUrl = "https://www.codefather.cn/logo.png";
        String fileName = "logo.png";
        ResourceDownloadTool resourceDownloadTool = new ResourceDownloadTool();
        String result = resourceDownloadTool.downloadResource(resourceUrl, fileName);
        assertNotNull(result);
    }
}
