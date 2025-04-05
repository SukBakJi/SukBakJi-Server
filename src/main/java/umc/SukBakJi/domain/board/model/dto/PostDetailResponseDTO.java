package umc.SukBakJi.domain.board.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class PostDetailResponseDTO {

    private String menu;
    private String title;
    private String content;
    private List<CommentDTO> comments;
    private String supportField; // New field
    private String hiringType;
    private Long commentCount;
    private Long memberId; // New field for member ID
    private Long views;

    @Getter
    @Setter
    public static class CommentDTO {
        private Long commentId;
        private String anonymousName;
        private String degreeLevel;
        private String content;
        private LocalDateTime createdDate;
        private Long memberId;
    }
}
