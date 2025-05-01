package umc.SukBakJi.domain.university.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import umc.SukBakJi.domain.university.converter.UnivConverter;
import umc.SukBakJi.domain.member.repository.MemberRepository;
import umc.SukBakJi.domain.university.model.dto.UnivRequestDTO;
import umc.SukBakJi.domain.university.model.dto.UnivResponseDTO;
import umc.SukBakJi.domain.member.model.entity.Member;
import umc.SukBakJi.domain.university.model.entity.UnivScheduleInfo;
import umc.SukBakJi.domain.university.model.entity.University;
import umc.SukBakJi.domain.common.entity.mapping.SetUniv;
import umc.SukBakJi.domain.university.repository.SetUnivRepository;
import umc.SukBakJi.domain.university.repository.UnivRepository;
import umc.SukBakJi.domain.university.repository.UnivScheduleInfoRepository;
import umc.SukBakJi.global.apiPayload.code.status.ErrorStatus;
import umc.SukBakJi.global.apiPayload.exception.GeneralException;
import umc.SukBakJi.global.apiPayload.exception.handler.MemberHandler;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@Slf4j
@RequiredArgsConstructor
public class CalenderService {

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
    public void updateUnivSchedule(Long memberId, Long univId, UnivRequestDTO.UpdateUnivDTO request) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.MEMBER_NOT_FOUND));

        University univ = univRepository.findById(univId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.INVALID_UNIVERSITY));

        SetUniv setUniv = setUnivRepository.findByMemberAndUniversity(member, univ)
                .orElseThrow(() -> new GeneralException(ErrorStatus.SET_UNIV_NOT_FOUND));

        setUniv.updateUnivSchedule(request.getSeason(), request.getMethod());

        setUnivRepository.save(setUniv);
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
    public void deleteSelectedUniv(Long memberId, UnivRequestDTO.DeleteSelectedUnivDTO univDTO){
        if (univDTO.getUnivIds() == null || univDTO.getUnivIds().isEmpty()) {
            throw new GeneralException(ErrorStatus.INVALID_UNIVERSITY);
        }

        memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));

        for (Long univId : univDTO.getUnivIds()) {
            univRepository.findById(univId)
                    .orElseThrow(() -> new GeneralException(ErrorStatus.INVALID_UNIVERSITY));
        }

        setUnivRepository.deleteByMemberIdAndUniversityIdIn(memberId, univDTO.getUnivIds());
    }

    @Transactional
    public void deleteAllUniv(Long memberId){
        memberRepository.findById(memberId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.MEMBER_NOT_FOUND));

        setUnivRepository.deleteByMemberId(memberId);
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
                    .flatMap(setUniv -> univScheduleInfoRepository.findByUniversityId(
                            setUniv.getUniversity().getId()).stream()) // 조건을 만족하는 UnivScheduleInfo 찾기
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
}
