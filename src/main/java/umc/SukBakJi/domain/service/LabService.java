package umc.SukBakJi.domain.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import umc.SukBakJi.domain.converter.LabConverter;
import umc.SukBakJi.domain.model.dto.LabRequestDTO;
import umc.SukBakJi.domain.model.entity.Lab;
import umc.SukBakJi.domain.model.entity.Member;
import umc.SukBakJi.domain.model.entity.enums.LabUpdateStatus;
import umc.SukBakJi.domain.model.entity.mapping.FavoriteLab;
import umc.SukBakJi.domain.model.entity.mapping.LabResearchTopic;
import umc.SukBakJi.domain.model.entity.mapping.LabUpdateRequest;
import umc.SukBakJi.domain.repository.FavoriteLabRepository;
import umc.SukBakJi.domain.model.dto.InterestTopicsDTO;
import umc.SukBakJi.domain.model.dto.LabDetailResponseDTO;
import umc.SukBakJi.domain.model.dto.LabResponseDTO;
import umc.SukBakJi.domain.model.entity.ResearchTopic;
import umc.SukBakJi.domain.repository.LabRepository;
import umc.SukBakJi.domain.repository.LabUpdateRequestRepository;
import umc.SukBakJi.domain.repository.MemberRepository;
import umc.SukBakJi.global.apiPayload.code.status.ErrorStatus;
import umc.SukBakJi.global.apiPayload.exception.GeneralException;
import umc.SukBakJi.global.apiPayload.exception.handler.LabHandler;
import umc.SukBakJi.global.apiPayload.exception.handler.MemberHandler;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class LabService {

    private final MemberRepository memberRepository;
    private final LabRepository labRepository;
    private final FavoriteLabRepository favoriteLabRepository;
    private final LabUpdateRequestRepository labUpdateRequestRepository;

    public List<LabResponseDTO> searchLabsByTopicName(String topicName) {
        List<Lab> labs = labRepository.findLabsByResearchTopicName(topicName);
        return labs.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    private LabResponseDTO convertToDTO(Lab lab) {
        List<String> researchTopics = lab.getLabResearchTopics().stream()
                .map(LabResearchTopic::getResearchTopic)
                .map(rt -> rt.getTopicName())
                .collect(Collectors.toList());

        return LabResponseDTO.builder()
                .labId(lab.getId())
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
                .map(rt -> rt.getTopicName())
                .collect(Collectors.toList());

        return new LabDetailResponseDTO(
                lab.getProfessorName(),
                lab.getUniversityName(),
                lab.getDepartmentName(),
                lab.getLabLink(),
                researchTopics,
                lab.getProfessorEmail() // Include professor email
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

        // 사용자의 즐겨찾기 목록 가져오기
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

    // 즐겨찾기 항목에서 삭제
    public void cancelFavoriteLab(Long memberId, LabRequestDTO.CancelLabDTO request) {
        // 존재하는 회원인지 조회
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));

        List<FavoriteLab> favoriteLabs = favoriteLabRepository.findByMemberAndLabIdIn(member, request.getLabIds());

        List<Long> requestedLabIds = new ArrayList<>(request.getLabIds());
        List<Long> existingFavoriteLabIds = favoriteLabs.stream()
                .map(favoriteLab -> favoriteLab.getLab().getId())
                .collect(Collectors.toList());

        // 존재하지 않는 연구실을 담은 리스트
        List<Long> nonExistingLabIds = new ArrayList<>(requestedLabIds);
        nonExistingLabIds.removeAll(existingFavoriteLabIds);

        // 즐겨찾기 항목에 존재하지 않을 경우 예외 처리
        if (!nonExistingLabIds.isEmpty()) {
            throw new GeneralException(ErrorStatus.FAVORITE_NOT_FOUND);
        }
        favoriteLabRepository.deleteAll(favoriteLabs);
    }

    // 연구실 문의 등록
    public void labUpdateRequest(Long memberId, LabRequestDTO.InquireLabDTO request) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));

        Lab lab = labRepository.findById(request.getLabId())
                .orElseThrow(() -> new LabHandler(ErrorStatus.LAB_NOT_FOUND));

        if (request.getContent() == null || request.getContent().trim().isEmpty()) {
            throw new LabHandler(ErrorStatus.INVALID_INQUIRY_CONTENT);
        }

        labUpdateRequestRepository.save(
                LabUpdateRequest.builder()
                        .member(member)
                        .lab(lab)
                        .requestCategory(request.getRequestCategory())
                        .content(request.getContent())
                        .labUpdateStatus(LabUpdateStatus.PENDING)
                        .build()
        );
    }
}