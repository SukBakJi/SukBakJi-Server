package umc.SukBakJi.domain.model.dto;

import lombok.Getter;
import lombok.Setter;
import umc.SukBakJi.domain.model.entity.enums.Menu;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class PostDetailResponseDTO {

    private String menu;
    private String title;
    private String content;
    private List<CommentDTO> comments;
    private Long commentCount;
    private Long views;

    @Getter
    @Setter
    public static class CommentDTO {
        private String anonymousName;
        private String degreeLevel;
        private String content;
        private LocalDateTime createdDate;
    }
}
