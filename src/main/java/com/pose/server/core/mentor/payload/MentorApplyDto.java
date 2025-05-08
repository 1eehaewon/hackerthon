package com.pose.server.core.mentor.payload;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MentorApplyDto {
    private Long applyId;

    private String mentorCareer;     // 멘토 경력 (ex: "백엔드 개발자 3년")

    private String affiliation;      // 소속 (ex: "카카오 엔터프라이즈")

    private String userId;
    private String name;
    private String email;
    private String tel;
    private String status;

    private LocalDateTime createdAt;
}
