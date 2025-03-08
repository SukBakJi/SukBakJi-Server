package umc.SukBakJi.domain.board.model.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PostSearchDTO {
    private Long postId;
    private String menu;
    private String boardName;
    private String title;
    private String content;
    private Long views;
    private long commentCount;
}
