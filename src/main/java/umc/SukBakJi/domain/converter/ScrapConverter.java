package umc.SukBakJi.domain.converter;

import umc.SukBakJi.domain.model.dto.ScrapPostDTO;
import umc.SukBakJi.domain.model.entity.mapping.Scrap;

import java.util.List;
import java.util.stream.Collectors;

public class ScrapConverter {

    public static List<ScrapPostDTO> toScrapPostDTOList(List<Scrap> scraps) {
        return scraps.stream()
                .map(scrap -> ScrapPostDTO.builder()
                        .menu(scrap.getPost().getBoard().getMenu())
                        .boardName(scrap.getPost().getBoard().getBoardName())
                        .title(scrap.getPost().getTitle())
                        .content(scrap.getPost().getContent())
                        .commentCount(scrap.getPost().getComments().size())
                        .views(scrap.getPost().getViews())
                        .build())
                .collect(Collectors.toList());
    }
}
