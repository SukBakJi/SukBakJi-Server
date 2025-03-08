package umc.SukBakJi.domain.board.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReportCommentRequestDTO {
    @NotNull(message = "댓글 ID는 필수입니다.")
    private Long commentId;

    @NotBlank(message = "신고 사유는 필수입니다.")
    private String reason;
}