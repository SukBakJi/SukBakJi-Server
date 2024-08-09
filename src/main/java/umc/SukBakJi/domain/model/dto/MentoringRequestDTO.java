package umc.SukBakJi.domain.model.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

public class MentoringRequestDTO {
    @Getter
    public static class applyMentor{
        @NotNull
        Long memberId;
    }
}
