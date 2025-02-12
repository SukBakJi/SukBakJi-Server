package umc.SukBakJi.domain.model.dto;

import lombok.*;

import java.util.List;

public class LabResponseDTO {
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LabPreviewResponseDTO {
        private Long labId;
        private String labName;
        private String universityName;
        private String departmentName;
        private String professorName;
        private List<String> researchTopics;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LabSearchResponseDTO {
        private List<LabPreviewResponseDTO> responseDTOList;
        private int totalNumber;
    }
}