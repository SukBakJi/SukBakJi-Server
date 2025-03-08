package umc.SukBakJi.domain.board.repository;

import umc.SukBakJi.domain.board.model.entity.Board;
import umc.SukBakJi.domain.common.entity.enums.Menu;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BoardRepository extends JpaRepository<Board, Long> {
    List<Board> findByMenu(Menu menu);
    List<Board> findByBoardNameAndMenu(String boardName, Menu menu);
    Optional<Board> findByMenuAndBoardName(Menu menu, String boardName);
}
