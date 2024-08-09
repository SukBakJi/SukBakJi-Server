package umc.SukBakJi.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import umc.SukBakJi.domain.model.entity.mapping.Mentoring;

public interface MentoringRepository extends JpaRepository<Mentoring, Long> {
}
