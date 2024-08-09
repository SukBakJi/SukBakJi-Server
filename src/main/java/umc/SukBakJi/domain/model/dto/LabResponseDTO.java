package umc.SukBakJi.domain.model.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class LabResponseDTO {
    private Long labId;
    private String labName;
    private String universityName;
    private String departmentName;
    private String professorName;
    private List<String> researchTopics;
}