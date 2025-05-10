package com.pose.server.core.member.api;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class HomeController {

    @GetMapping("/")
    public String root() {
        return "index";  // 또는 별도 루트 페이지
    }

    @GetMapping("/loginhome")
    public String loginhome(HttpSession session, Model model, RedirectAttributes redirectAttributes) {
        String userId = (String) session.getAttribute("user");
        String role = session.getAttribute("role").toString();

        if (userId != null) {
            model.addAttribute("user", userId); // 세션에 있는 사용자 정보 전달
            model.addAttribute("role", role);
        }

        if (userId == null) {
            // FlashAttribute에 메시지 저장 → 로그인 페이지에서 alert로 처리
            redirectAttributes.addFlashAttribute("alert", "로그인이 필요합니다.");
            return "redirect:/members/login";
        }

        return "indexG";
    }
}
