package umc.SukBakJi.domain.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import umc.SukBakJi.domain.model.entity.enums.Menu;

@Getter
@Setter
public class CreatePostRequestDTO {

    @NotNull(message = "Menu is required")
    private Menu menu;

    @NotBlank(message = "Board name is required")
    private String boardName;

    @NotBlank(message = "Title is required")
    private String title;

    @NotBlank(message = "Content is required")
    private String content;
}
