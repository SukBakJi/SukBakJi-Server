package umc.SukBakJi.domain.alarm.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import umc.SukBakJi.domain.alarm.repository.AlarmRepository;
import umc.SukBakJi.domain.alarm.converter.AlarmConverter;
import umc.SukBakJi.domain.common.entity.mapping.SetUniv;
import umc.SukBakJi.domain.member.repository.MemberRepository;
import umc.SukBakJi.domain.alarm.model.dto.AlarmRequestDTO;
import umc.SukBakJi.domain.alarm.model.dto.AlarmResponseDTO;
import umc.SukBakJi.domain.university.model.dto.UnivResponseDTO;
import umc.SukBakJi.domain.alarm.model.entity.Alarm;
import umc.SukBakJi.domain.member.model.entity.Member;
import umc.SukBakJi.domain.university.repository.SetUnivRepository;
import umc.SukBakJi.domain.university.repository.UnivRepository;
import umc.SukBakJi.global.apiPayload.code.status.ErrorStatus;
import umc.SukBakJi.global.apiPayload.exception.GeneralException;
import umc.SukBakJi.global.apiPayload.exception.handler.CalendarHandler;
import umc.SukBakJi.global.apiPayload.exception.handler.MemberHandler;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AlarmService {
    private final AlarmRepository alarmRepository;
    private final UnivRepository univRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public Alarm createAlarm(AlarmRequestDTO.createAlarm request){
        if(request.getDate() == null || request.getTime() == null){
            throw new GeneralException(ErrorStatus.INVALID_DATE);
        }

        Member member = memberRepository.findById(request.getMemberId())
                .orElseThrow(() -> new GeneralException(ErrorStatus.MEMBER_NOT_FOUND));

        Alarm alarm = AlarmConverter.toAlarm(request, member);

        List<Alarm> existingAlarm1 = alarmRepository.findByNameAndMember(alarm.getName(), alarm.getMember());
        List<Alarm> existingAlarm2 = alarmRepository.findByUnivNameAndMember(alarm.getUnivName(), alarm.getMember());
        if (!(existingAlarm1.isEmpty()) && !(existingAlarm2.isEmpty())) {
            throw new GeneralException(ErrorStatus.DUPLICATE_ALARM_NAME);
        }

        alarm = alarmRepository.save(alarm);

        return alarm;
    }

    @Transactional
    public AlarmResponseDTO.alarmDTO updateAlarm(Long alarmId, AlarmRequestDTO.updateAlarm request){
        Member member = memberRepository.findById(request.getMemberId())
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));

        Alarm existingAlarm = alarmRepository.findById(alarmId)
                .orElseThrow(() -> new CalendarHandler(ErrorStatus.INVALID_ALARM));

        boolean isNameChanged = !existingAlarm.getName().equals(request.getName());
        boolean isUnivNameChanged = !existingAlarm.getUnivName().equals(request.getUnivName());

        if ((isNameChanged || isUnivNameChanged) && (request.getName() != null || request.getUnivName() != null)) {
            boolean isDuplicate = alarmRepository.existsByNameAndMemberAndIdNot(request.getName(), member, alarmId) ||
                    alarmRepository.existsByUnivNameAndMemberAndIdNot(request.getUnivName(), member, alarmId);

            if (isDuplicate) {
                throw new GeneralException(ErrorStatus.DUPLICATE_ALARM_NAME);
            }
        }
        existingAlarm.updateAlarm(request.getName(), request.getUnivName(), request.getDate(), request.getTime(), request.getOnoff());

        return AlarmConverter.alarmDTO(existingAlarm);
    }

    @Transactional
    public void deleteAlarm(Long memberId, Long alarmId){
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));

        Alarm alarm = alarmRepository.findById(alarmId)
                .orElseThrow(() -> new CalendarHandler(ErrorStatus.INVALID_ALARM));

        if (!alarm.getMember().getId().equals(member.getId())) {
            throw new CalendarHandler(ErrorStatus.UNAUTHORIZED_ALARM_ACCESS);
        }

        alarmRepository.delete(alarm);
    }

    @Transactional
    public List<Alarm> getAlarmList(Long memberId){
        List<Alarm> alarmList = alarmRepository.findByMemberId(memberId);
        if(alarmList.isEmpty()){
            alarmList = null;
        }
        return alarmList;
    }

    @Transactional
    public Alarm onAlarm(Long alarmId){
        Alarm alarm = alarmRepository.findById(alarmId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.INVALID_ALARM));
        alarm.setOnoff(1L);
        alarm = alarmRepository.save(alarm);

        return alarm;
    }

    @Transactional
    public Alarm offAlarm(Long alarmId){
        Alarm alarm = alarmRepository.findById(alarmId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.INVALID_ALARM));
        alarm.setOnoff(0L);
        alarm = alarmRepository.save(alarm);

        return alarm;
    }

    public List<String> getAlarmUnivList() {
        return univRepository.findAllUniversityNames();
    }
}