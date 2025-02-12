package umc.SukBakJi.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import umc.SukBakJi.domain.model.entity.mapping.LabUpdateRequest;

public interface LabUpdateRequestRepository extends JpaRepository<LabUpdateRequest, Long> {
}
