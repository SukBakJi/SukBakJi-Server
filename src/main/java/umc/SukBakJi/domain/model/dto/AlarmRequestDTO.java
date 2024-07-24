package umc.SukBakJi.domain.model.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class AlarmRequestDTO {
    @Getter
    public static class createAlarm{
        @NotNull
        Long memberId;
        @NotNull
        String name;
        @NotNull
        String date;
        @NotNull
        String time;
    }
}
