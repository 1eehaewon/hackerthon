package com.pose.server.core.mentor.application;

import com.pose.server.core.mentor.domain.MentorApplyEntity;
import com.pose.server.core.mentor.infrastructure.MentorApplyRepository;
import com.pose.server.core.mentor.payload.MentorApplyDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MentorApplyService {
    private final MentorApplyRepository mentorApplyRepository;

    private final MemberRepository memberRepository;

    /**
     * 멘토 신청 등록
     */
    public void applyForMentor(Long memberId, MentorApplyDto dto) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));

        if (mentorApplyRepository.existsByMember(member)) {
            throw new IllegalStateException("이미 멘토 신청을 했습니다.");
        }

        MentorApplyEntity apply = MentorApplyEntity.builder()
                .member(member)
                .mentorCareer(dto.getMentor_career())
                .afflication(dto.getAfflication())
                .status(MentorApplyEntity.Status.PENDING)
                .build();

        mentorApplyRepository.save(apply);
    }

    /**
     * 멘토 신청 조회 (선택적)
     */
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("회원이 존재하지 않습니다."));
        return mentorApplyRepository.findByMember(member);
    }
}
