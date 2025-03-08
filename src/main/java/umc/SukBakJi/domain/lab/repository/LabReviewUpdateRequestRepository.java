package umc.SukBakJi.domain.lab.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import umc.SukBakJi.domain.common.entity.mapping.LabReviewUpdateRequest;

public interface LabReviewUpdateRequestRepository extends JpaRepository<LabReviewUpdateRequest, Long> {
}
