package umc.SukBakJi.domain.mentoring.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import umc.SukBakJi.domain.common.entity.mapping.Mentoring;

import java.util.List;

public interface MentoringRepository extends JpaRepository<Mentoring, Long> {
    List<Mentoring> findByMemberIdAndMentorId(Long memberId, Long mentorId);
}
