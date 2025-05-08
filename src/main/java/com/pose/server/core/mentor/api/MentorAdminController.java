package com.pose.server.core.mentor.api;

import com.pose.server.core.member.domain.MemberEntity;
import com.pose.server.core.mentor.application.MentorApplyService;
import com.pose.server.core.mentor.domain.MentorApplyEntity;
import org.springframework.ui.Model;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Controller
@RequestMapping("/admin/mentor")
@RequiredArgsConstructor
public class MentorAdminController {

    private final MentorApplyService mentorApplyService;

    /**
     * 관리자 전용 멘토 신청 목록
     */
    @GetMapping("/applications")
    public String viewApplications(@SessionAttribute(name = "user") MemberEntity loginUser,
                                   Model model) {
        if (!loginUser.isAdmin()) {
            return "error/403"; // 권한 없음
        }

        List<MentorApplyEntity> applications = mentorApplyService.getAllApplications();
        model.addAttribute("applications", applications);
        return "admin/mentor/applications";
    }

    /**
     * 멘토 신청 승인
     */
    @PostMapping("/{applyId}/approve")
    public String approve(@PathVariable Long applyId,
                          @SessionAttribute(name = "user") MemberEntity user) {
        if (!user.isAdmin()) return "error/403";
        mentorApplyService.approveApplication(applyId);
        return "redirect:/admin/mentor/applications";
    }

    /**
     * 멘토 신청 거절
     */
    @PostMapping("/{applyId}/reject")
    public String reject(@PathVariable Long applyId,
                         @SessionAttribute(name = "user") MemberEntity user) {
        if (!user.isAdmin()) return "error/403";
        mentorApplyService.rejectApplication(applyId);
        return "redirect:/admin/mentor/applications";
    }
}