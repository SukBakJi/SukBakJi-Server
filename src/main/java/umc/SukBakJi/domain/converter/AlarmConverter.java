package umc.SukBakJi.domain.converter;

import umc.SukBakJi.domain.model.dto.AlarmRequestDTO;
import umc.SukBakJi.domain.model.dto.AlarmResponseDTO;
import umc.SukBakJi.domain.model.entity.Alarm;
import umc.SukBakJi.domain.model.entity.Member;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class AlarmConverter {
    public static AlarmResponseDTO.createAlarmDTO toCreateAlarm(Alarm alarm){
        return AlarmResponseDTO.createAlarmDTO.builder()
                .alarmId(alarm.getId())
                .alarmUnivName(alarm.getUnivName())
                .alarmName(alarm.getName())
                .onoff(alarm.getOnoff())
                .memberId(alarm.getMember().getId())
                .build();
    }

    public static Alarm toAlarm(AlarmRequestDTO.createAlarm request, Member member){
        return Alarm.builder()
                .member(member)
                .univName(request.getUnivName())
                .name(request.getName())
                .date(request.getDate())
                .time(request.getTime())
                .onoff(request.getOnoff())
                .build();
    }

    public static AlarmResponseDTO.alarmDTO alarmDTO(Alarm alarm){
        return AlarmResponseDTO.alarmDTO.builder()
                .alarmId(alarm.getId())
                .alarmUnivName(alarm.getUnivName())
                .alarmName(alarm.getName())
                .alarmDate(alarm.getDate())
                .alarmTime(alarm.getTime())
                .onoff(alarm.getOnoff())
                .build();
    }

    public static AlarmResponseDTO.getAlarmListDTO getAlarmListDTO(Long memberId, List<Alarm> alarmList){
        if(alarmList == null){
            return AlarmResponseDTO.getAlarmListDTO.builder()
                    .memberId(memberId)
                    .alarmList(null)
                    .build();
        }
        List<AlarmResponseDTO.alarmDTO> getAlarmListDTOList = alarmList.stream()
                .map(AlarmConverter::alarmDTO).collect(Collectors.toList());
        return AlarmResponseDTO.getAlarmListDTO.builder()
                .memberId(memberId)
                .alarmList(getAlarmListDTOList)
                .build();
    }

    public static AlarmResponseDTO.turnOnOff turnOnOff(Alarm alarm){
        return AlarmResponseDTO.turnOnOff.builder()
                .alarmId(alarm.getId())
                .onoff(alarm.getOnoff())
                .build();
    }
}
