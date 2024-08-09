package umc.SukBakJi.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import umc.SukBakJi.domain.model.entity.Mentee;

public interface MenteeRepository extends JpaRepository<Mentee, Long> {
}
