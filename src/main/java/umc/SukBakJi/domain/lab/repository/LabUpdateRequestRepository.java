package umc.SukBakJi.domain.lab.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import umc.SukBakJi.domain.common.entity.mapping.LabUpdateRequest;

public interface LabUpdateRequestRepository extends JpaRepository<LabUpdateRequest, Long> {
}
