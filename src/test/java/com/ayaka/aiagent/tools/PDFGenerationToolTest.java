package com.ayaka.aiagent.tools;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class PDFGenerationToolTest {

    @Test
    void generatePDF() {

        PDFGenerationTool pdfGenerateTool = new PDFGenerationTool();
        String result = pdfGenerateTool.generatePDF("百度一下，你就知道.pdf", "https://www.baidu.com");
        assertNotNull(result);
    }
}