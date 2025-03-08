package umc.SukBakJi.domain.lab.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import umc.SukBakJi.domain.lab.converter.LabReviewConverter;
import umc.SukBakJi.domain.lab.model.dto.*;
import umc.SukBakJi.domain.member.model.entity.Member;
import umc.SukBakJi.domain.common.entity.enums.LabUpdateStatus;
import umc.SukBakJi.domain.common.entity.mapping.LabReviewUpdateRequest;
import umc.SukBakJi.domain.lab.repository.LabReviewUpdateRequestRepository;
import umc.SukBakJi.domain.member.repository.MemberRepository;
import umc.SukBakJi.global.apiPayload.code.status.ErrorStatus;
import umc.SukBakJi.global.apiPayload.exception.GeneralException;
import umc.SukBakJi.domain.lab.model.entity.Lab;
import umc.SukBakJi.domain.common.entity.mapping.LabReview;
import umc.SukBakJi.domain.lab.repository.LabRepository;
import umc.SukBakJi.domain.lab.repository.LabReviewRepository;
import umc.SukBakJi.global.apiPayload.exception.handler.LabHandler;
import umc.SukBakJi.global.apiPayload.exception.handler.MemberHandler;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LabReviewService {

    private final LabReviewUpdateRequestRepository labReviewUpdateRequestRepository;
    private final MemberRepository memberRepository;
    private final LabReviewRepository labReviewRepository;
    private final LabRepository labRepository;

    private final LabReviewConverter labReviewConverter;

    public LabReviewSummaryDTO getLabReviews(Long labId) {
        List<LabReview> reviews = labReviewRepository.findByLabId(labId);
        if (reviews.isEmpty()) {
            throw new GeneralException(ErrorStatus.LAB_REVIEW_NOT_FOUND);
        }

        List<LabReviewDetailsDTO> reviewDTOs = labReviewConverter.toDto(reviews);
        TriangleGraphData triangleGraphData = calculateTriangleGraphData(reviews);

        return LabReviewSummaryDTO.builder()
                .reviews(reviewDTOs)
                .triangleGraphData(triangleGraphData)
                .build();

    }

    private TriangleGraphData calculateTriangleGraphData(List<LabReview> reviews) {
        double leadershipSum = 0, salarySum = 0, autonomySum = 0;

        for (LabReview review : reviews) {
            leadershipSum += review.getLeadershipStyle().getValue();
            salarySum += review.getSalaryLevel().getValue();
            autonomySum += review.getAutonomy().getValue();
        }

        int totalReviews = reviews.size();
        return TriangleGraphData.builder()
                .leadershipAverage(leadershipSum / totalReviews)
                .salaryAverage(salarySum / totalReviews)
                .autonomyAverage(autonomySum / totalReviews)
                .build();
    }

    public LabReviewDetailsDTO createLabReview(LabReviewCreateDTO dto, Long labId, Long memberId) {
        if (dto.getContent() == null || dto.getContent().length() < 30) {
            throw new GeneralException(ErrorStatus.INVALID_REVIEW_CONTENT);
        }

        LabReview review = labReviewConverter.toEntity(dto, labId, memberId);

        Optional<LabReview> existingReview = labReviewRepository.findByLabAndMember(review.getLab(), review.getMember());
        if (existingReview.isPresent()) {
            throw new GeneralException(ErrorStatus.DUPLICATE_REVIEW);
        }

        review = labReviewRepository.save(review);
        return labReviewConverter.toDto(review);
    }

    public List<LabReviewDetailsDTO> getLabReviewList(int offset, Optional<Integer> limit) {
        List<LabReview> reviews;
        if (limit.isPresent()) {
            PageRequest pageRequest = PageRequest.of(offset, limit.get(), Sort.by(Sort.Direction.DESC, "createdAt"));
            reviews = labReviewRepository.findAll(pageRequest).getContent();
        } else {
            reviews = labReviewRepository.findAll(Sort.by(Sort.Direction.DESC, "createdAt"));
        }
        return reviews.stream().map(labReviewConverter::toDto).collect(Collectors.toList());
    }

    public List<LabReviewDetailsDTO> searchLabReviews(String professorName) {

        List<Lab> labs = labRepository.findByProfessorName(professorName);
        if (labs.isEmpty()) {
            throw new GeneralException(ErrorStatus.PROFESSOR_NOT_FOUND);
        }

        List<LabReview> reviews = labReviewRepository.findByLabIn(labs);
        if (reviews.isEmpty()) {
            throw new GeneralException(ErrorStatus.LAB_REVIEW_NOT_FOUND);
        }

        return labReviewConverter.toDto(reviews);
    }

    // 연구실 후기 문의 등록
    public void labReviewUpdateRequest(Long memberId, LabRequestDTO.InquireLabReviewDTO request) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));

        LabReview labReview = labReviewRepository.findById(request.getLabReviewId())
                .orElseThrow(() -> new LabHandler(ErrorStatus.LAB_REVIEW_NOT_FOUND));

        if (request.getContent() == null || request.getContent().trim().isEmpty()) {
            throw new LabHandler(ErrorStatus.INVALID_INQUIRY_CONTENT);
        }

        labReviewUpdateRequestRepository.save(
                LabReviewUpdateRequest.builder()
                        .member(member)
                        .labReview(labReview)
                        .content(request.getContent())
                        .labUpdateStatus(LabUpdateStatus.PENDING)
                        .build()
        );
    }
}
