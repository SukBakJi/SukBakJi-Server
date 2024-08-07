package umc.SukBakJi.domain.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import umc.SukBakJi.domain.converter.LabConverter;
import umc.SukBakJi.domain.model.entity.Lab;
import umc.SukBakJi.domain.model.entity.Member;
import umc.SukBakJi.domain.model.entity.mapping.FavoriteLab;
import umc.SukBakJi.domain.model.entity.mapping.LabResearchTopic;
import umc.SukBakJi.domain.repository.FavoriteLabRepository;
import umc.SukBakJi.domain.model.dto.InterestTopicsDTO;
import umc.SukBakJi.domain.model.dto.LabDetailResponseDTO;
import umc.SukBakJi.domain.model.dto.LabResponseDTO;
import umc.SukBakJi.domain.model.entity.ResearchTopic;
import umc.SukBakJi.domain.repository.LabRepository;
import umc.SukBakJi.domain.repository.MemberRepository;
import umc.SukBakJi.global.apiPayload.code.status.ErrorStatus;
import umc.SukBakJi.global.apiPayload.exception.GeneralException;
import umc.SukBakJi.global.apiPayload.exception.handler.MemberHandler;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class LabService {

    private final MemberRepository memberRepository;
    private final LabRepository labRepository;
    private final FavoriteLabRepository favoriteLabRepository;

    public List<LabResponseDTO> searchLabsByTopicName(String topicName) {
        List<Lab> labs = labRepository.findLabsByResearchTopicName(topicName);
        return labs.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    private LabResponseDTO convertToDTO(Lab lab) {
        List<String> researchTopics = lab.getLabResearchTopics().stream()
                .map(LabResearchTopic::getResearchTopic)
                .map(ResearchTopic::getTopicName)
                .collect(Collectors.toList());

        return LabResponseDTO.builder()
                .labName(lab.getLabName())
                .universityName(lab.getUniversityName())
                .professorName(lab.getProfessorName())
                .departmentName(lab.getDepartmentName())
                .researchTopics(researchTopics)
                .build();
    }

    public LabDetailResponseDTO getLabDetail(Long labId) {
        Lab lab = labRepository.findById(labId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.LAB_NOT_FOUND));

        List<String> researchTopics = lab.getLabResearchTopics().stream()
                .map(LabResearchTopic::getResearchTopic)
                .map(ResearchTopic::getTopicName)
                .collect(Collectors.toList());

        return new LabDetailResponseDTO(
                lab.getProfessorName(),
                lab.getUniversityName(),
                lab.getDepartmentName(),
                lab.getLabLink(),
                researchTopics
        );
    }

    public InterestTopicsDTO getInterestTopics(Member member) {
        List<String> topics = member.getMemberResearchTopics()
                .stream()
                .map(memberResearchTopic -> "#" + memberResearchTopic.getResearchTopic().getTopicName())
                .collect(Collectors.toList());

        return InterestTopicsDTO.builder()
                .topics(topics)
                .build();
    }

    public List<LabResponseDTO> getFavoriteLabs(Long memberId) {
        // 존재하는 회원인지 조회
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));

        // 사용자의 즐겨찾기 목록을 가져오기
        List<FavoriteLab> favoriteLabs = favoriteLabRepository.findByMember(member);

        return favoriteLabs.stream()
                .map(FavoriteLab::getLab) // FavoriteLab에서 연구실 객체 추출
                .map(LabConverter::getFavoriteLabInfo)
                .collect(Collectors.toList());
    }

    // 연구실 즐겨찾기 추가 및 취소
    public Boolean toggleFavoriteLab(Long memberId, Long labId) {
        // 존재하는 회원인지 조회
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));

        // 존재하는 연구실인지 조회
        Lab lab = labRepository.findById(labId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.LAB_NOT_FOUND));

        // 즐겨찾기 여부 확인
        boolean isFavorite = favoriteLabRepository.existsByMemberAndLab(member, lab);

        if (isFavorite) {
            // 즐겨찾기에 추가한 상태라면 즐겨찾기 취소
            favoriteLabRepository.deleteByMemberAndLab(member, lab);
            return false;
        } else {
            // 즐겨찾기를 하지 않은 상태라면 즐겨찾기 추가
            try {
                FavoriteLab favoriteLab = LabConverter.toFavoriteLab(member, lab);
                favoriteLabRepository.save(favoriteLab);
                return true;
            } catch (Exception e) {
                throw new GeneralException(ErrorStatus.FAVORITE_ADD_FAILED);
            }
        }
    }
}