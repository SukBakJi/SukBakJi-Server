package umc.SukBakJi.domain.model.dto;

import lombok.Builder;
import lombok.Getter;
import umc.SukBakJi.domain.model.entity.ResearchTopic;

import java.util.List;

@Getter
@Builder
public class LabReviewDetailsDTO {
    private String universityName;
    private String labName;
    private String professorName;
    private String professorProfile;
    private String professorAcademic;
    private List<ResearchTopic> researchKeywords;
    private String content;
    private List<String> tags;
    private String createdAt;
}
