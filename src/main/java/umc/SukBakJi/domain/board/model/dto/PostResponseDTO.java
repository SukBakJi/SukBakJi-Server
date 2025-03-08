package umc.SukBakJi.domain.board.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostResponseDTO {
    private Long postId;
    private String title;
    private String content;
    private Long views;
    private String createdAt;
    private String updatedAt;
    private Long boardId;
    private Long memberId;
}
