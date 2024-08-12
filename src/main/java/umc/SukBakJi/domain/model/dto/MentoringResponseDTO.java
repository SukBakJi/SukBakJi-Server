package umc.SukBakJi.domain.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

public class MentoringResponseDTO {
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class setMentorDTO{
        Long memberId;
        String message;
    }
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class getMentorDTO{
        Long memberId;
        List<MentorDTO> mentorList;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MentorDTO{
        Long mentorId;
        String univName;
        String profName;
        String deptName;
        List<String> researchTopic;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class setMentoringDTO{
        Long mentorId;
        Long menteeId;
        String message;
    }
}
