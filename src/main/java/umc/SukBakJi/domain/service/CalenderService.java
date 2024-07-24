package umc.SukBakJi.domain.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import umc.SukBakJi.domain.converter.AlarmConverter;
import umc.SukBakJi.domain.converter.SetUnivConverter;
import umc.SukBakJi.domain.model.dto.AlarmRequestDTO;
import umc.SukBakJi.domain.model.dto.UnivRequestDTO;
import umc.SukBakJi.domain.model.entity.Alarm;
import umc.SukBakJi.domain.model.entity.Member;
import umc.SukBakJi.domain.repository.AlarmRepository;
import umc.SukBakJi.domain.repository.MemberRepository;
import umc.SukBakJi.domain.repository.UnivRepository;
import umc.SukBakJi.global.apiPayload.code.status.ErrorStatus;
import umc.SukBakJi.global.apiPayload.exception.GeneralException;

import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class CalenderService {

    @Autowired
    private AlarmRepository alarmRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Transactional
    public Alarm createAlarm(AlarmRequestDTO.createAlarm request){
        if(request.getDate() == null || request.getTime() == null){
            throw new GeneralException(ErrorStatus.INVALID_DATE);
        }

        Member member = memberRepository.findById(request.getMemberId())
                .orElseThrow(() -> new GeneralException(ErrorStatus.MEMBER_NOT_FOUND));

        Alarm alarm = AlarmConverter.toAlarm(request, member);

        Optional<Alarm> existingAlarm = alarmRepository.findByNameAndMember(alarm.getName(), alarm.getMember());
        if (existingAlarm.isPresent()) {
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


//    public UnivRequestDTO setUniv(UnivRequestDTO.setUniv request){
//        LabReview review = labReviewConverter.toEntity(dto);
//
//        Optional<LabReview> existingReview = labReviewRepository.findByLabAndMember(review.getLab(), review.getMember());
//    }
}
