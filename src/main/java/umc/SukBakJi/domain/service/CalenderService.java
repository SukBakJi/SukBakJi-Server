package umc.SukBakJi.domain.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import umc.SukBakJi.domain.converter.AlarmConverter;
import umc.SukBakJi.domain.converter.UnivConverter;
import umc.SukBakJi.domain.model.dto.AlarmRequestDTO;
import umc.SukBakJi.domain.model.dto.AlarmResponseDTO;
import umc.SukBakJi.domain.model.dto.UnivRequestDTO;
import umc.SukBakJi.domain.model.dto.UnivResponseDTO;
import umc.SukBakJi.domain.model.entity.Alarm;
import umc.SukBakJi.domain.model.entity.Member;
import umc.SukBakJi.domain.model.entity.UnivScheduleInfo;
import umc.SukBakJi.domain.model.entity.University;
import umc.SukBakJi.domain.model.entity.mapping.SetUniv;
import umc.SukBakJi.domain.repository.*;
import umc.SukBakJi.global.apiPayload.code.status.ErrorStatus;
import umc.SukBakJi.global.apiPayload.exception.GeneralException;
import umc.SukBakJi.global.apiPayload.exception.handler.CalendarHandler;
import umc.SukBakJi.global.apiPayload.exception.handler.MemberHandler;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class CalenderService {

    @Autowired
    private AlarmRepository alarmRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private SetUnivRepository setUnivRepository;

    @Autowired
    private UnivRepository univRepository;

    @Autowired
    private UnivScheduleInfoRepository univScheduleInfoRepository;

    @Transactional
    public UnivResponseDTO.getUnivIdDTO getUnivId(Long univId){
        Optional<University> univ = univRepository.findById(univId);
        return UnivResponseDTO.getUnivIdDTO.builder()
                .univId(univId)
                .univName(univ.get().getName())
                .build();
    }

    @Transactional
    public List<UnivResponseDTO.searchListDTO> getSearchList(String keyword){
        List<String> universityList = univRepository.findByKeyWord(keyword);
        return universityList.stream()
                .map(UnivConverter::toUnivList)
                .collect(Collectors.toList());
    }
    @Transactional
    public List<String> getMethodList(Long univId){
        List<String> univScheduleInfoList = univScheduleInfoRepository.findAllByUniversityId(univId);
        return univScheduleInfoList;
    }

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

//    @Transactional -> 학교 한 번에 여러 개 저장하기
//    public void setUniv(UnivRequestDTO.setUnivList request){
//        List<UnivRequestDTO.setUniv> setUnivList = request.getSetUnivList();
//        setUnivList.stream()
//            .map(setUniv -> {
//                University univ = univRepository.findById(setUniv.getUnivId())
//                        .orElseThrow(() -> new GeneralException(ErrorStatus.INVALID_UNIVERSITY));
//
//                Member member = memberRepository.findById(request.getMemberId())
//                        .orElseThrow(() -> new GeneralException(ErrorStatus.MEMBER_NOT_FOUND));
//
//                SetUniv s = UnivConverter.toSetUniv(setUniv, member, univ);
//                return s;
//            })
//            .forEach(s -> setUnivRepository.save(s));
//    }
    @Transactional
    public SetUniv setUniv(UnivRequestDTO.setUniv request){
        University univ = univRepository.findById(request.getUnivId())
                .orElseThrow(() -> new GeneralException(ErrorStatus.INVALID_UNIVERSITY));

        Member member = memberRepository.findById(request.getMemberId())
                .orElseThrow(() -> new GeneralException(ErrorStatus.MEMBER_NOT_FOUND));

        SetUniv setUniv = UnivConverter.toSetUniv(request, member, univ);
        setUniv = setUnivRepository.save(setUniv);
        return setUniv;
    }

    @Transactional
    public void deleteUniv(UnivRequestDTO.setUniv request){
        univRepository.findById(request.getUnivId())
                .orElseThrow(() -> new GeneralException(ErrorStatus.INVALID_UNIVERSITY));

        memberRepository.findById(request.getMemberId())
                .orElseThrow(() -> new GeneralException(ErrorStatus.MEMBER_NOT_FOUND));

        setUnivRepository.deleteByMemberIdAndUniversityIdAndSeasonAndMethod(request.getMemberId(), request.getUnivId(), request.getSeason(), request.getMethod());
        return;
    }

    @Transactional
    public List<SetUniv> getUnivList(Long memberId){
        List<SetUniv> univList = setUnivRepository.findByMemberId(memberId);
        if(univList.isEmpty()){
            univList = null;
        }
        return univList;
    }

    @Transactional
    public void setSchedule(UnivRequestDTO.setScheduleList request){
        List<UnivRequestDTO.setSchedule> setUnivList = request.getSetScheduleList();
        setUnivList.stream()
                .map(s -> {
                    University univ = univRepository.findByName(s.getUnivName())
                            .orElseThrow(() -> new GeneralException(ErrorStatus.INVALID_UNIVERSITY));

                    // 들어온 값에 해당하는 행을 조회하고, 이에 대해서 showing 업데이트하는 로직
                    SetUniv setUniv = setUnivRepository.findByMemberIdAndUniversityIdAndMethodAndSeason(request.getMemberId(), univ.getId(), s.getMethod(), s.getSeason())
                            .orElseThrow(() -> new GeneralException(ErrorStatus.MEMBER_NOT_FOUND));

                    memberRepository.findById(request.getMemberId())
                            .orElseThrow(() -> new GeneralException(ErrorStatus.MEMBER_NOT_FOUND));

                    setUniv.setShowing(s.getShowing());
                    return setUniv;
                })
                .forEach(setUniv -> setUnivRepository.save(setUniv));
    }

    @Transactional
    public List<UnivScheduleInfo> getScheduleList(Long memberId){
        List<SetUniv> setUnivList = setUnivRepository.findAllByMemberId(memberId);
        if (setUnivList == null){
            return null;
        }
        else {
            // 선택한 일정만 필터링
            List<UnivScheduleInfo> univScheduleInfoList = setUnivList.stream()
                    .filter(setUniv -> setUniv.getShowing() == 1)
                    .flatMap(setUniv -> univScheduleInfoRepository.findByUniversityIdAndSeasonAndMethod(
                            setUniv.getUniversity().getId(), setUniv.getSeason(), setUniv.getMethod()).stream()) // 조건을 만족하는 UnivScheduleInfo 찾기
                    .collect(Collectors.toList()); // 리스트로 수집
            return univScheduleInfoList;
        }
    }

    @Transactional
    public List<UnivScheduleInfo> getSpeciDateScheduleList(Long memberId, String date){
        List<SetUniv> setUnivList = setUnivRepository.findAllByMemberId(memberId);
        if (setUnivList == null){
            return null;
        }
        else {
            // 선택한 일정만 필터링
            List<UnivScheduleInfo> univScheduleInfoList = setUnivList.stream()
                    .filter(setUniv -> setUniv.getShowing() == 1)
                    .flatMap(setUniv -> univScheduleInfoRepository.findByDate(date).stream()) // 조건을 만족하는 UnivScheduleInfo 찾기
                    .collect(Collectors.toList()); // 리스트로 수집
            return univScheduleInfoList;
        }
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
}
