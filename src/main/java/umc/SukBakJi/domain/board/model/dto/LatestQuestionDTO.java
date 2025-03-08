package umc.SukBakJi.domain.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import umc.SukBakJi.global.entity.enums.Menu;

@Getter
@AllArgsConstructor
public class LatestQuestionDTO {
    private Menu menu;
    private String title;
}
