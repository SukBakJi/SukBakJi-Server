package umc.SukBakJi.domain.model.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PostListDTO {
    private Long postId;
    private String title;
    private String content;
    private Long views;
    private String boardName;
    private String menu;
    private long commentCount;
}
