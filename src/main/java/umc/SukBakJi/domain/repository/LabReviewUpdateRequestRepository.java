package umc.SukBakJi.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import umc.SukBakJi.domain.model.entity.mapping.LabReviewUpdateRequest;

public interface LabReviewUpdateRequestRepository extends JpaRepository<LabReviewUpdateRequest, Long> {
}
