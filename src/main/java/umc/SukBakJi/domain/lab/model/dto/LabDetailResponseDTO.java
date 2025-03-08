package umc.SukBakJi.domain.lab.model.dto;

import lombok.*;

import java.util.List;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class LabDetailResponseDTO {
    private String professorName;
    private String universityName;
    private String departmentName;
    private String labLink;
    private List<String> researchTopics;
    private String professorEmail;
}