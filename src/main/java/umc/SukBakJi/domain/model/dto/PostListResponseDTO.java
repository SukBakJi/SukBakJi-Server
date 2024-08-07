package umc.SukBakJi.domain.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostListResponseDTO {
    private Long postId;
    private String title;
    private String previewContent;
    private Long commentCount;
    private Long views;
}
