package umc.SukBakJi.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import umc.SukBakJi.domain.model.entity.University;

public interface UnivRepository extends JpaRepository<University, Long> {
}
