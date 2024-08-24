package umc.SukBakJi.domain.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class CommentResponseDTO {
    private Long commentId;
    private String content;
    private String nickname;
    private Long memberId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
