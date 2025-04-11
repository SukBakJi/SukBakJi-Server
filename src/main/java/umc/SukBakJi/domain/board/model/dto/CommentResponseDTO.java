package umc.SukBakJi.domain.board.model.dto;

import lombok.*;

import java.time.LocalDateTime;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CommentResponseDTO {
    private Long commentId;
    private String content;
    private String nickname;
    private Long memberId;
    private Long postId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
