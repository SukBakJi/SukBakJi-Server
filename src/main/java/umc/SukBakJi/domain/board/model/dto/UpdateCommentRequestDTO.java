package umc.SukBakJi.domain.board.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateCommentRequestDTO {

    @NotNull(message = "Comment ID is required")
    private Long commentId;

    @NotBlank(message = "Content is required")
    private String content;
}