package umc.SukBakJi.domain.block.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import umc.SukBakJi.domain.block.model.entity.MemberBlock;

import java.util.List;
import java.util.Optional;

@Repository
public interface BlockRepository extends JpaRepository<MemberBlock, Long> {

    // 차단 중인지 확인
    Optional<MemberBlock> findByBlockerIdAndBlockedIdAndIsActiveTrue(Long blockerId, Long blockedId);

    // 차단한 유저 목록 (id만)
    @Query("SELECT mb.blocked.id FROM MemberBlock mb WHERE mb.blocker.id = :blockerId AND mb.isActive = true")
    List<Long> findBlockedIdsByBlockerId(@Param("blockerId") Long blockerId);

    // 차단 이력 전체 (isActive 무관)
    List<MemberBlock> findByBlockerId(Long blockerId);
}
