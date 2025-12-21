package com.ayaka.aiagent.tools;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class fileOperationToolTest {

    @Test
    void radFiles() {
        FileOperationTool fileOperationTool = new FileOperationTool();
        String readFiles = fileOperationTool.readFiles("test.txt");
        Assertions.assertNotNull(readFiles);
    }

    @Test
    void writeFiles() {
        FileOperationTool fileOperationTool = new FileOperationTool();
        String writeFiles = fileOperationTool.writeFiles("test.txt", "测试文件写入");
        Assertions.assertNotNull(writeFiles);
    }
}