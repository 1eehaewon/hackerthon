package com.pose.server.core.mentor.api;

import com.pose.server.core.mentor.application.MentorApplyService;
import com.pose.server.core.mentor.payload.MentorApplyDto;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/mentor/apply")
@RequiredArgsConstructor
public class MentorApplyController {
    private final MentorApplyService mentorApplyService;

    /**
     * 멘토 신청 등록
     */
    @PostMapping("")
    public ResponseEntity<String> applyForMentor(@RequestBody MentorApplyDto dto,
                                                 HttpSession session) {

        Long memberId = (Long) session.getAttribute("loginMemberId");
        if (memberId == null) {
            return ResponseEntity.status(401).body("로그인이 필요합니다.");
        }

        mentorApplyService.applyForMentor(memberId, dto);
        return ResponseEntity.ok("멘토 신청이 완료되었습니다.");
    }

}
