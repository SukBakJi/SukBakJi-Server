package umc.SukBakJi.domain.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import umc.SukBakJi.domain.model.entity.enums.Menu;

@Getter
@Setter
public class CreateJobPostRequestDTO {

    @NotNull(message = "Menu is required")
    private Menu menu;

    @NotBlank(message = "Board name is required")
    private String boardName;

    @NotBlank(message = "지원분야 is required")
    private String supportField;

    @NotBlank(message = "직무 is required")
    private String job;

    @NotBlank(message = "채용형태 is required")
    private String hiringType;

    @NotBlank(message = "최종학력 is required")
    private String finalEducation;

    @NotBlank(message = "Title is required")
    private String title;

    @NotBlank(message = "Content is required")
    private String content;
}
