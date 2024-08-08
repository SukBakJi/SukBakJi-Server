package umc.SukBakJi.domain.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import umc.SukBakJi.domain.converter.AlarmConverter;
import umc.SukBakJi.domain.converter.UnivConverter;
import umc.SukBakJi.domain.model.dto.AlarmRequestDTO;
import umc.SukBakJi.domain.model.dto.UnivRequestDTO;
import umc.SukBakJi.domain.model.dto.UnivResponseDTO;
import umc.SukBakJi.domain.model.entity.Alarm;
import umc.SukBakJi.domain.model.entity.Member;
import umc.SukBakJi.domain.model.entity.University;
import umc.SukBakJi.domain.model.entity.mapping.SetUniv;
import umc.SukBakJi.domain.repository.*;
import umc.SukBakJi.global.apiPayload.code.status.ErrorStatus;
import umc.SukBakJi.global.apiPayload.exception.GeneralException;

import java.util.List;
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
    public List<Alarm> getAlarmList(Long memberId){
        List<Alarm> alarmList = alarmRepository.findByMemberId(memberId);
        if(alarmList.isEmpty()){
            alarmList = null;
        }
        return alarmList;
    }

//    @Transactional
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
