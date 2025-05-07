package com.pose.server.core.member.api;

import com.pose.server.core.member.application.MemberService;
import com.pose.server.core.member.domain.MemberEntity;
import com.pose.server.core.member.payload.LoginDTO;
import com.pose.server.core.member.payload.MemberDTO;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder;

    @GetMapping("/home")
    public String home(HttpSession session, Model model) {
        String userId = (String) session.getAttribute("user");

        if (userId != null) {
            model.addAttribute("user", userId); // 세션에 있는 사용자 정보 전달
        }

        return "member/home"; // home.html
    }

    @GetMapping("/join")
    public String showJoinForm(Model model) {
        model.addAttribute("member", new MemberDTO());
        return "member/join"; // templates/member/join.html
    }

    @PostMapping("/join")
    public String handleJoin(@ModelAttribute("member") @Valid MemberDTO memberDTO, BindingResult result) {
        if (result.hasErrors()) {
            return "member/join";
        }

        memberService.join(memberDTO);
        return "redirect:/members/join-success";
    }

    @GetMapping("/join-success")
    public String joinSuccess() {
        return "member/join-success"; // templates/member/join-success.html
    }

    @GetMapping("/login")
    public String showLoginForm(Model model) {
        model.addAttribute("login", new LoginDTO());
        return "member/login"; // templates/member/login.html
    }

    @PostMapping("/login")
    public String login(LoginDTO loginDTO, Model model, HttpSession session) {
        boolean success = memberService.login(loginDTO);

        if (success) {
            // 로그인 성공 시 세션에 사용자 정보 저장 (예: userId)
            session.setAttribute("user", loginDTO.getUserId());
            return "redirect:/members/home"; // 로그인 후 리다이렉트할 페이지
        } else {
            // 로그인 실패 시 오류 메시지 전달
            model.addAttribute("loginError", "아이디 또는 비밀번호가 일치하지 않습니다.");
            return "member/login"; // 로그인 폼 다시 보여주기
        }
    }
}
