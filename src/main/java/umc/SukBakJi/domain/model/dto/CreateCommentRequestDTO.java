package umc.SukBakJi.domain.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateCommentRequestDTO {

    @NotNull(message = "Post ID is required")
    private Long postId;

    @NotNull(message = "Member ID is required")
    private Long memberId;

    @NotBlank(message = "Content is required")
    private String content;
}
