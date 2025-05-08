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


    //메인페이지
    @GetMapping("")
    public String home() {
        return "member/home"; // home.html
    }

    @GetMapping("/loginhome")
    public String loginhome(HttpSession session, Model model) {
        String userId = (String) session.getAttribute("user");
        String role = session.getAttribute("role").toString();

        if (userId != null) {
            model.addAttribute("user", userId); // 세션에 있는 사용자 정보 전달
            model.addAttribute("role", role);
        }

        return "member/home"; // home.html
    }

    //회원가입
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


    //로그인
    @GetMapping("/login")
    public String showLoginForm(Model model) {
        model.addAttribute("login", new LoginDTO());
        return "member/login"; // templates/member/login.html
    }

    @PostMapping("/login")
    public String login(LoginDTO loginDTO, Model model, HttpSession session) {
        MemberEntity memberEntity = memberService.login(loginDTO);

        if (memberEntity.getUserId() != null) {
            // 로그인 성공 시 세션에 사용자 정보 저장 (예: userId)
            session.setAttribute("user", memberEntity.getUserId());
            session.setAttribute("role", memberEntity.getRole());
            return "redirect:/members/loginhome"; // 로그인 후 리다이렉트할 페이지
        } else {
            // 로그인 실패 시 오류 메시지 전달
            model.addAttribute("loginError", "아이디 또는 비밀번호가 일치하지 않습니다.");
            return "member/login"; // 로그인 폼 다시 보여주기
        }
    }

    //로그아웃
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();  // 세션 무효화 (로그아웃)
        return "redirect:/members/login";  // 로그인 페이지로 리다이렉트
    }

    //회원정보 수정
    @GetMapping("/update")
    public String showUpdateForm(HttpSession session, Model model) {
        String loginId = (String) session.getAttribute("user");

        if (loginId == null) {
            return "redirect:/members/login";
        }

        // 서비스로부터 최신 MemberEntity 조회
        MemberEntity member = memberService.findByUserId(loginId);

        MemberDTO memberDTO = MemberDTO.builder()
                .userId(member.getUserId())
                .name(member.getName())
                .email(member.getEmail())
                .addr(member.getAddr())
                .tel(member.getTel())
                .build();

        model.addAttribute("memberDTO", memberDTO);
        return "member/update-form";
    }

    @PostMapping("/update")
    public String updateMember(@ModelAttribute MemberDTO memberDTO, HttpSession session, Model model) {
        String userId = (String) session.getAttribute("user");

        if (userId == null) {
            return "redirect:/members/login";
        }

        // 기존 회원 정보 조회
        MemberEntity member = memberService.findByUserId(userId);
        if (member == null) {
            model.addAttribute("error", "회원 정보를 찾을 수 없습니다.");
            return "member/update-form";
        }

        // 회원 정보 업데이트
        member.setName(memberDTO.getName());
        member.setEmail(memberDTO.getEmail());
        member.setAddr(memberDTO.getAddr());
        member.setTel(memberDTO.getTel());

        memberService.update(member);  // 저장 로직은 서비스에 위임

        model.addAttribute("message", "회원 정보가 성공적으로 수정되었습니다.");
        return "redirect:/members/loginhome";  // 또는 수정 완료 페이지로 이동
    }
}
