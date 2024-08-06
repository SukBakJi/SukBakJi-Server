package umc.SukBakJi.domain.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class LabResponseDTO {
    private Long labId;
    private String universityName;
    private String department;
    private String professorName;
    private List<String> researchTopics;
}
