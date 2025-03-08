package umc.SukBakJi.domain.board.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import umc.SukBakJi.domain.common.entity.enums.Menu;

@Getter
@AllArgsConstructor
@Builder
public class HotBoardPostDTO {
    private Long postId;
    private Menu menu;
    private String boardName;
    private String title;
    private String content;
    private int commentCount;
    private Long views;
}