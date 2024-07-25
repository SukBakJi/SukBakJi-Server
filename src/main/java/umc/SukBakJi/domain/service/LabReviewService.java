package umc.SukBakJi.domain.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import umc.SukBakJi.domain.converter.LabReviewConverter;
import umc.SukBakJi.global.apiPayload.code.status.ErrorStatus;
import umc.SukBakJi.global.apiPayload.exception.GeneralException;
import umc.SukBakJi.domain.model.dto.LabReviewCreateDTO;
import umc.SukBakJi.domain.model.dto.LabReviewDetailsDTO;
import umc.SukBakJi.domain.model.entity.Lab;
import umc.SukBakJi.domain.model.entity.mapping.LabReview;
import umc.SukBakJi.domain.repository.LabRepository;
import umc.SukBakJi.domain.repository.LabReviewRepository;

import java.util.List;
import java.util.Optional;

@Service
public class LabReviewService {

    @Autowired
    private LabReviewRepository labReviewRepository;

    @Autowired
    private LabRepository labRepository;

    @Autowired
    private LabReviewConverter labReviewConverter;

    public LabReviewDetailsDTO getLabReviewDetails(Long reviewId) {
        LabReview review = labReviewRepository.findById(reviewId).orElseThrow(() -> new GeneralException(ErrorStatus.LAB_REVIEW_NOT_FOUND));

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

    public List<LabReviewDetailsDTO> getLabReviewList(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<LabReview> reviews = labReviewRepository.findAll(pageRequest);
        if (reviews.isEmpty()) {
            throw new GeneralException(ErrorStatus.LAB_REVIEW_NOT_FOUND);
        }

        return labReviewConverter.toDto(reviews.getContent());
    }

    public List<LabReviewDetailsDTO> searchLabReviews(String professorName, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);

        List<Lab> labs = labRepository.findByProfessorName(professorName);
        if (labs.isEmpty()) {
            throw new GeneralException(ErrorStatus.PROFESSOR_NOT_FOUND);
        }

        List<LabReview> reviews = labReviewRepository.findByLabIn(labs, pageRequest);
        if (reviews.isEmpty()) {
            throw new GeneralException(ErrorStatus.LAB_REVIEW_NOT_FOUND);
        }

        return labReviewConverter.toDto(reviews);
    }
}
