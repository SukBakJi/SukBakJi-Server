package umc.SukBakJi.domain.board.service;

import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import umc.SukBakJi.domain.board.model.dto.BoardLikeResponseDTO;
import umc.SukBakJi.domain.board.model.dto.CreateBoardRequestDTO;
import umc.SukBakJi.domain.board.model.entity.Board;
import umc.SukBakJi.domain.member.model.entity.Member;
import umc.SukBakJi.domain.common.entity.enums.Menu;
import umc.SukBakJi.domain.common.entity.mapping.BoardLike;
import umc.SukBakJi.domain.common.entity.mapping.BoardLikeId;
import umc.SukBakJi.domain.board.repository.BoardLikeRepository;
import umc.SukBakJi.domain.board.repository.BoardRepository;
import umc.SukBakJi.domain.member.repository.MemberRepository;
import umc.SukBakJi.global.apiPayload.code.status.ErrorStatus;
import umc.SukBakJi.global.apiPayload.exception.GeneralException;
import umc.SukBakJi.global.apiPayload.exception.handler.MemberHandler;
import umc.SukBakJi.global.filter.BadWordFilter;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class BoardService {
    private final BoardRepository boardRepository;
    private final BoardLikeRepository boardLikeRepository;
    private final MemberRepository memberRepository;
    private final BadWordFilter badWordFilter;

    public List<String> getBoardNamesByMenu(Menu menu) {
        List<Board> boards = boardRepository.findByMenu(menu);
        return boards.stream()
                .map(Board::getBoardName)
                .collect(Collectors.toList());
    }

    public Board createBoard(CreateBoardRequestDTO request) {

        // 이름 중복 검사
        List<Board> existingBoards = boardRepository.findByBoardNameAndMenu(request.getBoardName(), Menu.자유);
        if (!existingBoards.isEmpty()) {
            throw new IllegalArgumentException("A board with this name already exists in the 자유 menu.");
        }

        // 비속어 필터링 (게시판 제목, 설명 모두 필터링)
        if(badWordFilter.containsBadWord(request.getBoardName()) || badWordFilter.containsBadWord(request.getDescription())) {
            throw new GeneralException(ErrorStatus.BAD_WORD_DETECTED);
        }

        // 정상 처리
        Board board = new Board();
        board.setBoardName(request.getBoardName());
        board.setDescription(request.getDescription());
        board.setMenu(Menu.자유);
        return boardRepository.save(board);
    }

    public boolean addFavoriteBoard(Long memberId, Long boardId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.MEMBER_NOT_FOUND));
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.BOARD_NOT_FOUND));

        BoardLikeId boardLikeId = new BoardLikeId(memberId, boardId);

        if (boardLikeRepository.existsById(boardLikeId)) {
            throw new GeneralException(ErrorStatus.FAVORITE_ALREADY_EXISTS);
        } else {
            BoardLike boardLike = new BoardLike(boardLikeId, member, board);
            boardLikeRepository.save(boardLike);
            return true; // Favorited
        }
    }

    public boolean removeFavoriteBoard(Long memberId, Long boardId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.MEMBER_NOT_FOUND));
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.BOARD_NOT_FOUND));

        BoardLikeId boardLikeId = new BoardLikeId(memberId, boardId);

        if (!boardLikeRepository.existsById(boardLikeId)) {
            throw new GeneralException(ErrorStatus.FAVORITE_NOT_FOUND);
        } else {
            boardLikeRepository.deleteById(boardLikeId);
            return true; // Unfavorited
        }
    }

    public List<BoardLikeResponseDTO> getFavoriteBoardList(Long memberId) {
        List<BoardLike> boardLikeList = boardLikeRepository.findWithBoardByMemberId(memberId);

        return boardLikeList.stream()
                .map(boardLike -> {
                    Board board = boardLike.getBoard();
                    return BoardLikeResponseDTO.builder()
                            .label(board.getMenu().toString())
                            .boardId(board.getBoardId())
                            .boardName(board.getBoardName())
                            .build();
                })
                .collect(Collectors.toList());
    }
}
