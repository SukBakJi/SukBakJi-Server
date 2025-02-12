package umc.SukBakJi.domain.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import umc.SukBakJi.domain.model.entity.enums.RequestCategory;

import java.util.List;

public class LabRequestDTO {
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CancelLabDTO {
        private List<Long> labIds;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class InquireLabDTO {
        private Long labId;
        private RequestCategory requestCategory;
        private String content;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class InquireLabReviewDTO {
        private Long labReviewId;
        private String content;
    }
}