package com.ayaka.imagesearchmcpserver.tools;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ImageSearchToolTest {

    @Test
    void search() {
        ImageSearchTool imageSearchTool = new ImageSearchTool();
        String result = imageSearchTool.search("cat");
        System.out.println(result);
    }
}