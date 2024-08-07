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
        LabResponseDTO dto = new LabResponseDTO();
        dto.setLabId(lab.getId());
        dto.setUniversityName(lab.getUniversityName());
        dto.setDepartment(lab.getDepartmentName());
        dto.setProfessorName(lab.getProfessorName());
        List<String> topicNames = lab.getResearchTopics().stream()
                .map(ResearchTopic::getTopicName)
                .collect(Collectors.toList());

        dto.setResearchTopics(topicNames);
        return dto;
    }

    public LabDetailResponseDTO getLabDetail(Long labId) {
        Lab lab = labRepository.findById(labId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.LAB_NOT_FOUND));

        List<String> researchTopics = lab.getResearchTopics().stream()
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

    // 연구실 즐겨찾기 추가
    public void addFavoriteLab(Long memberId, Long labId) {
        // 존재하는 회원인지 조회
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));

        // 존재하는 연구실인지 조회
        Lab lab = labRepository.findById(labId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.LAB_NOT_FOUND));

        if (!favoriteLabRepository.existsByMemberAndLab(member, lab)) {
            FavoriteLab favoriteLab = LabConverter.toFavoriteLab(member, lab);
            favoriteLabRepository.save(favoriteLab);
        }
    }

    // 연구실 즐겨찾기 취소
    public void cancelFavoriteLab(Long memberId, Long labId) {
        // 존재하는 회원인지 조회
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));

        // 존재하는 연구실인지 조회
        Lab lab = labRepository.findById(labId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.LAB_NOT_FOUND));

        if (favoriteLabRepository.existsByMemberAndLab(member, lab)) {
            favoriteLabRepository.deleteByMemberAndLab(member, lab);
        }

        // 즐겨찾기 여부 확인
        boolean exists = favoriteLabRepository.existsByMemberAndLab(member, lab);

        if (!exists) {
            throw new GeneralException(ErrorStatus.FAVORITE_NOT_FOUND);
        }

        // 즐겨찾기에서 삭제
        favoriteLabRepository.deleteByMemberAndLab(member, lab);
    }
}