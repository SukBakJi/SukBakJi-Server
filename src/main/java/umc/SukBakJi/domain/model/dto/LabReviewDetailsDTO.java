package umc.SukBakJi.domain.model.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class LabReviewDetailsDTO {
    private String universityName;
    private String labName;
    private String content;
    private List<String> tags;
    private String createdAt;
}
