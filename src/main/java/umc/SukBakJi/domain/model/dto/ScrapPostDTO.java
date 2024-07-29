package umc.SukBakJi.domain.model.dto;

import lombok.*;
import umc.SukBakJi.domain.model.entity.enums.Menu;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ScrapPostDTO {
    private Menu menu;
    private String boardName;
    private String title;
    private String content;
    private int commentCount;
    private Long views;
}
