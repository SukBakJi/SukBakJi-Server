package umc.SukBakJi.domain.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import umc.SukBakJi.domain.converter.LabReviewConverter;
import umc.SukBakJi.global.apiPayload.code.status.ErrorStatus;
import umc.SukBakJi.global.apiPayload.exception.GeneralException;
import umc.SukBakJi.domain.model.dto.LabReviewCreateDTO;
import umc.SukBakJi.domain.model.dto.LabReviewDetailsDTO;
import umc.SukBakJi.domain.model.entity.Lab;
import umc.SukBakJi.domain.model.entity.Member;
import umc.SukBakJi.domain.model.entity.mapping.LabReview;
import umc.SukBakJi.domain.repository.LabRepository;
import umc.SukBakJi.domain.repository.LabReviewRepository;
import umc.SukBakJi.domain.repository.MemberRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class LabReviewService {

    @Autowired
    private LabReviewRepository labReviewRepository;

    @Autowired
    private LabReviewConverter labReviewConverter;

    public LabReviewDetailsDTO getLabReviewDetails(Long reviewId) {
        LabReview review = labReviewRepository.findById(reviewId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.LAB_REVIEW_NOT_FOUND));

        return labReviewConverter.toDto(review);

    }

    public LabReviewDetailsDTO createLabReview(LabReviewCreateDTO dto) {
        if (dto.getContent() == null || dto.getContent().length() < 10) {
            throw new GeneralException(ErrorStatus.INVALID_REVIEW_CONTENT);
        }

        LabReview review = labReviewConverter.toEntity(dto);

        Optional<LabReview> existingReview = labReviewRepository.findByLabAndMember(review.getLab(), review.getMember());
        if (existingReview.isPresent()) {
            throw new GeneralException(ErrorStatus.DUPLICATE_REVIEW);
        }

        review = labReviewRepository.save(review);
        return labReviewConverter.toDto(review);
    }

}
