package com.e_messenger.code.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/test")
public class ViewResolver {
    @GetMapping("/web-socket")
    public String testWebSocket() {
        return "testWebSocket";
    }
}
