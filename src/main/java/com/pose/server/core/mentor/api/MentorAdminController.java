package com.pose.server.core.mentor.api;

import com.pose.server.core.member.domain.MemberEntity;
import com.pose.server.core.mentor.application.MentorApplyService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/mentor")
@RequiredArgsConstructor
public class MentorAdminController {

    private final MentorApplyService mentorApplyService;

    private boolean isAdmin(HttpSession session) {
        String userId = (String) session.getAttribute("user");
        MemberEntity.Role role = (MemberEntity.Role) session.getAttribute("role");
        return userId != null && role == MemberEntity.Role.ADMIN;
    }
    /**
     * 관리자 전용 멘토 신청 목록
     */
    @GetMapping
    public String viewMentorList(HttpSession session, Model model) {

        model.addAttribute("mentorList", mentorApplyService.getAllApplications());

        // 'user'나 'role' 값이 없으면 로그인하지 않은 사용자로 간주하고 리다이렉트
        if (!isAdmin(session)) {
            return "redirect:/login"; // 로그인 페이지로 리다이렉트
        }

        // 세션이 유효한 경우 관리자 페이지로
        if (isAdmin(session)) {
            return "admin/mentorList"; // 멘토 목록을 보여주는 페이지로 리다이렉트
        }

        // 관리자가 아닌 사용자는 접근을 막거나 다른 페이지로 리다이렉트
        return "redirect:/members/login"; // 접근 제한된 페이지로 리다이렉트
    }



    @PostMapping("/{id}/approve")
    public String approveMentor(@PathVariable Long id, HttpSession session, RedirectAttributes redirectAttributes) {
        if (!isAdmin(session)) {
            redirectAttributes.addFlashAttribute("error", "접근 권한이 없습니다.");
            return "redirect:/login";
        }

        mentorApplyService.approveApplication(id);
        redirectAttributes.addFlashAttribute("message", "승인되었습니다.");
        return "redirect:/admin/mentor";
    }

    @PostMapping("/{id}/reject")
    public String rejectMentor(@PathVariable Long id, HttpSession session, RedirectAttributes redirectAttributes) {
        if (!isAdmin(session)) {
            redirectAttributes.addFlashAttribute("error", "접근 권한이 없습니다.");
            return "redirect:/login";
        }

        mentorApplyService.rejectApplication(id);
        redirectAttributes.addFlashAttribute("message", "거절되었습니다.");
        return "redirect:/admin/mentor";
    }
}