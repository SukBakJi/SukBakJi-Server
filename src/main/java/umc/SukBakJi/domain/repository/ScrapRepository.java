package umc.SukBakJi.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import umc.SukBakJi.domain.model.entity.mapping.Scrap;
import umc.SukBakJi.domain.model.entity.mapping.ScrapId;

import java.util.List;

@Repository
public interface ScrapRepository extends JpaRepository<Scrap, ScrapId> {
    @Query("SELECT s FROM Scrap s WHERE s.id.memberId = :memberId")
    List<Scrap> findByMemberId(@Param("memberId") Long memberId);
}
