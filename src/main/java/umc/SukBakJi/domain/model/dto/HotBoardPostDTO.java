package umc.SukBakJi.domain.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import umc.SukBakJi.domain.model.entity.enums.Menu;

@Getter
@AllArgsConstructor
public class HotBoardPostDTO {
    private Menu menu;
    private String boardName;
    private String title;
    private String content;
    private int commentCount;
    private Long views;
}