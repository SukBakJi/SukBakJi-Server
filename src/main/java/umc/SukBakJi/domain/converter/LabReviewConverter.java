package umc.SukBakJi.domain.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import umc.SukBakJi.domain.model.dto.LabReviewCreateDTO;
import umc.SukBakJi.domain.model.dto.LabReviewDetailsDTO;
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

    public LabReview toEntity(LabReviewCreateDTO dto) {
        Lab lab = labRepository.findById(dto.getLabId())
                .orElseThrow(() -> new GeneralException(ErrorStatus.LAB_NOT_FOUND));
        Member member = memberRepository.findById(dto.getMemberId())
                .orElseThrow(() -> new GeneralException(ErrorStatus.MEMBER_NOT_FOUND_FOR_REVIEW));

        return new LabReview(
                lab,
                member,
                dto.getContent(),
                dto.getAtmosphere(),
                dto.getThesisGuidance(),
                dto.getLeadershipStyle(),
                dto.getSalaryLevel(),
                dto.getGraduationDifficulty()
        );
    }

    public LabReviewDetailsDTO toDto(LabReview review) {
        Lab lab = review.getLab();
        return LabReviewDetailsDTO.builder()
                .universityName(review.getLab().getUniversityName())
                .labName(review.getLab().getLabName())
                .professorName(lab.getProfessorName())
                .professorProfile(lab.getProfessorProfile())
                .professorAcademic(lab.getProfessorAcademic())
                .researchKeywords(lab.getResearchTopics())
                .content(review.getContent())
                .tags(Arrays.asList(
                        review.getAtmosphere().name(),
                        review.getThesisGuidance().name(),
                        review.getLeadershipStyle().name(),
                        review.getSalaryLevel().name(),
                        review.getGraduationDifficulty().name()
                ))
                .createdAt(review.getCreatedAt().toString())
                .build();
    }

    public List<LabReviewDetailsDTO> toDto(List<LabReview> reviews) {
        if (reviews == null || reviews.isEmpty()) {
            throw new GeneralException(ErrorStatus.LAB_REVIEW_NOT_FOUND);
        }

        return reviews.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }
}
