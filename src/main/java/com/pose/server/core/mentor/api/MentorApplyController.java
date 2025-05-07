package com.pose.server.core.mentor.api;

import com.pose.server.core.mentor.application.MentorApplyService;
import com.pose.server.core.mentor.payload.MentorApplyDto;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("api/mentor/apply")
@RequiredArgsConstructor
public class MentorApplyController {
    private final MentorApplyService mentorApplyService;

    /**
     * [GET] 멘토 신청 폼 화면 보여주기
     * 로그인한 사용자가 폼에 접근할 수 있도록 MentorApplyDto 빈 객체를 모델에 담아 전달
     */
    @GetMapping
    public String applyForm(Model model) {
        model.addAttribute("mentorApplyDto", new MentorApplyDto());
        return "mentor/applyForm"; // thymeleaf 템플릿 위치
    }

    /**
     * [POST] 멘토 신청 처리
     * 로그인한 사용자 정보와 신청 폼에서 입력한 정보를 이용해 서비스 로직 호출
     *
     * @param loginUser 세션에서 가져온 로그인 사용자 정보
     * @param dto       사용자 입력값 (경력, 소속 등)
     */
    @PostMapping
    public String apply(@SessionAttribute(name = "loginUser", required = true) MemberEntity loginUser,
                        @ModelAttribute MentorApplyDto dto,
                        RedirectAttributes redirectAttributes) {
        try {
            mentorApplyService.applyForMentor(loginUser.getUserId(), dto);
            redirectAttributes.addFlashAttribute("message", "멘토 신청이 완료되었습니다.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/mentor/apply";
    }

}
