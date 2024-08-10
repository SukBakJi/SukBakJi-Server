package umc.SukBakJi.domain.model.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

public class MentoringRequestDTO {
    @Getter
    public static class applyMentor{
        @NotNull
        Long memberId;
        @NotNull
        String univName;
        @NotNull
        String dept;
        @NotNull
        String profName;
    }

    @Getter
    public static class applyMentoring{
        @NotNull
        Long memberId; // menteeId가 memberId가 된다. 신청한 사람 = member = mentee
        @NotNull
        Long mentorId;
        @NotNull
        String subject;
        @NotNull
        String question;
    }
}
