package com.pose.server.core.board.payload;


import com.pose.server.core.board.domain.BoardStatus;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BoardResponseDTO {

    private Long boardId;

    private Long memberId;

    private String userId;

    private String title;

    private String content;

    private String image;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private BoardStatus boardStatus;

    private String mentorId;

    private String name;

    private List<ReplyResponseDTO> replyByPost;
}

