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
        String univName;
        @NotNull
        String name;
        @NotNull
        String date;
        @NotNull
        String time;
        @NotNull
        Long onoff;
    }

    @Getter
    public static class updateAlarm{
        @NotNull
        Long memberId;
        @NotNull
        String univName;
        @NotNull
        String name;
        @NotNull
        String date;
        @NotNull
        String time;
        @NotNull
        Long onoff;
    }

    @Getter
    public static class viewAlarmList{
        @NotNull
        Long memberId;
    }

    @Getter
    public static class turnAlarm{
        @NotNull
        Long alarmId;
    }
}
