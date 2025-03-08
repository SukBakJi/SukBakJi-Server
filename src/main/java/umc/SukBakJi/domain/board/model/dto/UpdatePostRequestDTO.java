package umc.SukBakJi.domain.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdatePostRequestDTO {
    private String title;
    private String content;
    private String supportField;  // Optional
    private String job;           // Optional
    private String hiringType;    // Optional
    private String finalEducation;// Optional
}