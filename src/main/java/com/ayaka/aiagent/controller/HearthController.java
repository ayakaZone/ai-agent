package com.ayaka.aiagent.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/hearth")
public class HearthController {
    @GetMapping
    public String health() {
        return "OK";
    }
}
