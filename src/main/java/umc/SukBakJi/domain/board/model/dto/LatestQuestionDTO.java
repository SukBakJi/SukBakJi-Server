package umc.SukBakJi.domain.board.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import umc.SukBakJi.domain.common.entity.enums.Menu;

@Getter
@AllArgsConstructor
public class LatestQuestionDTO {
    private Long postId;
    private Menu menu;
    private String title;
}
