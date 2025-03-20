package umc.SukBakJi.domain.board.model.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateBoardRequestDTO {

    @NotBlank(message = "Please write the board name")
    private String boardName;

    @NotBlank(message = "Please write the description")
    private String description;
}