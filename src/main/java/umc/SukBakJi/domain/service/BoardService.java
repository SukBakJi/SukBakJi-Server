package umc.SukBakJi.domain.service;

import umc.SukBakJi.domain.model.dto.CreateBoardRequest;
import umc.SukBakJi.domain.model.entity.Board;
import umc.SukBakJi.domain.model.entity.enums.Menu;
import umc.SukBakJi.domain.repository.BoardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BoardService {
    private final BoardRepository boardRepository;

    @Autowired
    public BoardService(BoardRepository boardRepository) {
        this.boardRepository = boardRepository;
    }

    public List<String> getBoardNamesByMenu(Menu menu) {
        List<Board> boards = boardRepository.findByMenu(menu);
        return boards.stream()
                .map(Board::getBoardName)
                .collect(Collectors.toList());
    }

    public Board createBoard(CreateBoardRequest request) {
        List<Board> existingBoards = boardRepository.findByBoardNameAndMenu(request.getBoardName(), Menu.자유);
        if (!existingBoards.isEmpty()) {
            throw new IllegalArgumentException("A board with this name already exists in the 자유 menu.");
        }

        Board board = new Board();
        board.setBoardName(request.getBoardName());
        board.setDescription(request.getDescription());
        board.setMenu(Menu.자유);
        return boardRepository.save(board);
    }
}
