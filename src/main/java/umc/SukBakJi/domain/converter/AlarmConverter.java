package umc.SukBakJi.domain.converter;

import umc.SukBakJi.domain.model.dto.AlarmRequestDTO;
import umc.SukBakJi.domain.model.dto.AlarmResponseDTO;
import umc.SukBakJi.domain.model.entity.Alarm;
import umc.SukBakJi.domain.model.entity.Member;

import java.util.Optional;

public class AlarmConverter {
    public static AlarmResponseDTO.createAlarmDTO toCreateAlarm(Alarm alarm){
        return AlarmResponseDTO.createAlarmDTO.builder()
                .alarmId(alarm.getId())
                .alarmName(alarm.getName())
                .memberId(alarm.getMember().getId())
                .build();
    }

    public static Alarm toAlarm(AlarmRequestDTO.createAlarm request, Member member){
        return Alarm.builder()
                .member(member)
                .name(request.getName())
                .date(request.getDate())
                .time(request.getTime())
                .build();
    }
}
