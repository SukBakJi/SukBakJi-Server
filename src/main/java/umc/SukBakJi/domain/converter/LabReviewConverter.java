package umc.SukBakJi.domain.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import umc.SukBakJi.domain.model.dto.LabReviewCreateDTO;
import umc.SukBakJi.domain.model.dto.LabReviewDetailsDTO;
import umc.SukBakJi.domain.model.dto.LabReviewSummaryDTO;
import umc.SukBakJi.domain.model.dto.TriangleGraphData;
import umc.SukBakJi.domain.model.entity.Lab;
import umc.SukBakJi.domain.model.entity.Member;
import umc.SukBakJi.domain.model.entity.mapping.LabReview;
import umc.SukBakJi.domain.repository.LabRepository;
import umc.SukBakJi.domain.repository.MemberRepository;
import umc.SukBakJi.global.apiPayload.code.status.ErrorStatus;
import umc.SukBakJi.global.apiPayload.exception.GeneralException;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class LabReviewConverter {
    @Autowired
    private LabRepository labRepository;

    @Autowired
    private MemberRepository memberRepository;

    public LabReview toEntity(LabReviewCreateDTO dto, Long labId, Long memberId) {
        Lab lab = labRepository.findById(labId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.LAB_NOT_FOUND));
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.MEMBER_NOT_FOUND_FOR_REVIEW));

        return new LabReview(
                lab,
                member,
                dto.getContent(),
                dto.getLeadershipStyle(),
                dto.getSalaryLevel(),
                dto.getAutonomy()
        );
    }

    public LabReviewDetailsDTO toDto(LabReview labReview) {
        return LabReviewDetailsDTO.builder()
                .universityName(labReview.getLab().getUniversityName()) // 대학교명 추가
                .departmentName(labReview.getLab().getLabName()) // 과 이름 추가
                .professorName(labReview.getLab().getProfessorName()) // 교수 이름 추가
                .content(labReview.getContent())
                .leadershipStyle(labReview.getLeadershipStyle())
                .salaryLevel(labReview.getSalaryLevel())
                .autonomy(labReview.getAutonomy())
                .build();
    }

    public List<LabReviewDetailsDTO> toDto(List<LabReview> labReviews) {
        return labReviews.stream().map(this::toDto).collect(Collectors.toList());
    }
}
