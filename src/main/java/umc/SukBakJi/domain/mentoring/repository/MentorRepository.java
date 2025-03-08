package umc.SukBakJi.domain.mentoring.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import umc.SukBakJi.domain.mentoring.model.entity.Mentor;

import java.util.List;

public interface MentorRepository extends JpaRepository<Mentor, Long> {
    List<Mentor> findByMemberId(Long memberId);
}
