package umc.SukBakJi.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import umc.SukBakJi.domain.model.entity.mapping.BoardLike;
import umc.SukBakJi.domain.model.entity.mapping.BoardLikeId;

import java.util.List;

public interface BoardLikeRepository extends JpaRepository<BoardLike, BoardLikeId> {
    List<BoardLike> findByMemberId(@Param("memberId") Long memberId);
}
