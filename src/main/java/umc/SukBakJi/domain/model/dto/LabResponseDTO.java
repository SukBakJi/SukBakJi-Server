package umc.SukBakJi.domain.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

public class LabResponseDTO {
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class getFavoriteLabDTO {
        private String labName;
        private String professorName;
        private String universityName;
        private String departmentName;
        private List<String> researchTopics;
    }
}
