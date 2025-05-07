package com.pose.server.core.mentor.application;

import com.pose.server.core.mentor.domain.MentorApplyEntity;
import com.pose.server.core.mentor.infrastructure.MentorApplyRepository;
import com.pose.server.core.mentor.payload.MentorApplyDto;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MentorApplyService {
    private final MentorApplyRepository mentorApplyRepository;
    private final MemberRepository memberRepository; // 회원 정보 조회용

    /**
     * 멘토 신청 처리 로직
     *
     * @param userId 로그인된 사용자 ID
     * @param dto    신청 폼에서 입력한 경력, 소속 정보
     */
    @Transactional
    public void applyForMentor(String userId, MentorApplyDto dto) {
        // 1. 사용자 조회
        MemberEntity member = memberRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        // 2. 기존에 신청한 이력이 있는지 확인
        if (mentorApplyRepository.findByMember(member).isPresent()) {
            throw new IllegalStateException("이미 멘토 신청을 하셨습니다.");
        }

        // 3. 신청 엔티티 생성 및 저장
        MentorApplyEntity apply = MentorApplyEntity.builder()
                .member(member)
                .mentorCareer(dto.getMentorCareer())
                .affiliation(dto.getAffiliation())
                .status(MentorApplyEntity.Status.PENDING) // 기본 상태는 대기중
                .build();

        mentorApplyRepository.save(apply);
    }


    /**
     * 사용자의 멘토 신청 내역 조회
     * (마이페이지에서 신청 상태 확인용 등)
     */
    public Optional<MentorApplyEntity> getMyApplication(String userId) {
        MemberEntity member = memberRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));
        return mentorApplyRepository.findByMember(member);
    }

    // 관리자 승인 / 거절
    // 두개의 엔티티를 다 바꿔서 dirty checking -> 추후 수정
    public void approveApplication(Long applyId) {
        MentorApplyEntity apply = mentorApplyRepository.findById(applyId)
                .orElseThrow(() -> new IllegalArgumentException("신청 내역이 없습니다."));
        apply.setStatus(MentorApplyEntity.Status.APPROVED);
        // 멘토 권한 부여
        MemberEntity member = apply.getMember();
        member.promoteToMentor(); // role = MENTOR
    }

    public void rejectApplication(Long applyId) {
        MentorApplyEntity apply = mentorApplyRepository.findById(applyId)
                .orElseThrow(() -> new IllegalArgumentException("신청 내역이 없습니다."));
        apply.setStatus(MentorApplyEntity.Status.REJECTED);
    }

    // 멘토 신청 목록
    public List<MentorApplyEntity> getAllApplications() {
        return mentorApplyRepository.findAll();
    }
}
