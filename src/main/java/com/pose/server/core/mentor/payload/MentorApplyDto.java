package com.pose.server.core.mentor.payload;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MentorApplyDto {

    private String mentorCareer;     // 멘토 경력 (ex: "백엔드 개발자 3년")

    private String affiliation;      // 소속 (ex: "카카오 엔터프라이즈")
}
