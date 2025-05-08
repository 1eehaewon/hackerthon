package com.pose.server.core.member.api;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String root() {
        return "member/home";  // 또는 별도 루트 페이지
    }
}
