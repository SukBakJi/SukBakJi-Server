package umc.SukBakJi.domain.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class LabDetailResponseDTO {
    private String professorName;
    private String universityName;
    private String department;
    private String professorAcademic;
    private String professorProfile;
    private String labLink;
    private List<String> researchTopics;
}
