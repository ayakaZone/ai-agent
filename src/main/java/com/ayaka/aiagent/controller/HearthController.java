package com.ayaka.aiagent.controller;

import com.ayaka.aiagent.common.BaseResponse;
import com.ayaka.aiagent.common.ResultUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/hearth")
public class HearthController {
    @GetMapping
    public BaseResponse<String> health() {
        return ResultUtils.success("ok");
    }
}
