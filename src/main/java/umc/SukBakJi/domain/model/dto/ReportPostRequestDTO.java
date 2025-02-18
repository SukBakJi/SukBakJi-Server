package umc.SukBakJi.domain.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReportPostRequestDTO {
    @NotNull(message = "게시글 ID는 필수입니다.")
    private Long postId;

    @NotBlank(message = "신고 사유는 필수입니다.")
    private String reason;
}