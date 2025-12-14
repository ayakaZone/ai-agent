package com.ayaka.aiagent.tools;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ScrapeWebPageTest {

    @Test
    void scrapeWebPage() {
        String url = "https://www.baidu.com";
        ScrapeWebPageTool scrapeWebPageTool = new ScrapeWebPageTool();
        String scrapeWebPage1 = scrapeWebPageTool.scrapeWebPage(url);
        assertNotNull(scrapeWebPage1);
    }
}