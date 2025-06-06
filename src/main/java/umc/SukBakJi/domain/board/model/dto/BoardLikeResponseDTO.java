package umc.SukBakJi.domain.board.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BoardLikeResponseDTO {
    private String label;
    private Long boardId;
    private String boardName;
}