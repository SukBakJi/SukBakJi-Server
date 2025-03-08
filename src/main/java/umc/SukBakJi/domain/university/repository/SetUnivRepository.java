package umc.SukBakJi.domain.university.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import umc.SukBakJi.domain.member.model.entity.Member;
import umc.SukBakJi.domain.university.model.entity.University;
import umc.SukBakJi.domain.common.entity.mapping.SetUniv;

import java.util.List;
import java.util.Optional;

public interface SetUnivRepository extends JpaRepository<SetUniv, Long> {
    List<SetUniv> findByMemberId(Long memberId);
    Optional<SetUniv> findByMemberIdAndUniversityIdAndMethodAndSeason(Long memberId, Long univId, String method, String season);
    Optional<SetUniv> findByMemberAndUniversity(Member member, University university);
    List<SetUniv> findAllByMemberId(Long memberId);
    void deleteByMemberIdAndUniversityIdAndSeasonAndMethod(Long memberId, Long univId, String season, String method);
    void deleteByMemberIdAndUniversityIdIn(Long memberId, List<Long> univIds);
    void deleteByMemberId(Long memberId);
}
