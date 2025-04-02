package umc.SukBakJi.domain.alarm.model.dto;

import lombok.*;

import java.util.List;

public class AlarmResponseDTO {
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class createAlarmDTO{
        Long alarmId;
        String alarmUnivName;
        String alarmName;
        Long onoff;
        Long memberId;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class getAlarmListDTO{
        Long memberId;
        List<AlarmResponseDTO.alarmDTO> alarmList;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class alarmDTO{
        Long alarmId;
        String alarmUnivName;
        String alarmName;
        String alarmDate;
        String alarmTime;
        Long onoff;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class turnOnOff{
        Long alarmId;
        Long onoff;
    }
}
