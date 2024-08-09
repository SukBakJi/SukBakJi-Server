package umc.SukBakJi.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import umc.SukBakJi.domain.model.entity.Member;
import umc.SukBakJi.domain.model.entity.University;
import umc.SukBakJi.domain.model.entity.mapping.SetUniv;

import java.util.List;
import java.util.Optional;

public interface SetUnivRepository extends JpaRepository<SetUniv, Long> {
    List<SetUniv> findByMemberId(Long memberId);
    Optional<SetUniv> findByMemberIdAndUniversityIdAndMethodAndSeason(Long memberId, Long univId, String method, String season);
    List<SetUniv> findAllByMemberId(Long memberId);
}
