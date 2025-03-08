package umc.SukBakJi.domain.board.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import umc.SukBakJi.domain.common.entity.mapping.BoardLike;
import umc.SukBakJi.domain.common.entity.mapping.BoardLikeId;

import java.util.List;

public interface BoardLikeRepository extends JpaRepository<BoardLike, BoardLikeId> {
    List<BoardLike> findByMemberId(@Param("memberId") Long memberId);
    boolean existsById(BoardLikeId id);
    void deleteById(BoardLikeId id);
}
