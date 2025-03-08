package umc.SukBakJi.domain.board.controller;

import jakarta.validation.Valid;
import org.springframework.validation.BindingResult;
import umc.SukBakJi.domain.board.model.dto.CreateBoardRequestDTO;
import umc.SukBakJi.domain.board.model.entity.Board;
import umc.SukBakJi.domain.common.entity.enums.Menu;
import umc.SukBakJi.domain.board.service.BoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import umc.SukBakJi.global.apiPayload.ApiResponse;
import umc.SukBakJi.global.security.jwt.JwtTokenProvider;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/boards")
public class BoardController {
    private final BoardService boardService;
    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    public BoardController(BoardService boardService, JwtTokenProvider jwtTokenProvider) {
        this.boardService = boardService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @GetMapping("/{menu}")
    public ResponseEntity<List<String>> getBoardNamesByMenu(@PathVariable Menu menu) {
        List<String> boardNames = boardService.getBoardNamesByMenu(menu);
        return ResponseEntity.ok(boardNames);
    }

    @PostMapping("/create")
    public ResponseEntity<?> createBoard(@Valid @RequestBody CreateBoardRequestDTO request, BindingResult result) {
        if (result.hasErrors()) {
            String errors = result.getAllErrors().stream()
                    .map(error -> error.getDefaultMessage())
                    .collect(Collectors.joining(", "));
            return ResponseEntity.badRequest().body(errors);
        }

        try {
            Board createdBoard = boardService.createBoard(request);
            return ResponseEntity.ok(createdBoard);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/{boardId}/favorite/add")
    public ResponseEntity<ApiResponse<String>> addFavoriteBoard(@RequestHeader("Authorization") String token,
                                                                @PathVariable Long boardId) {
        String jwtToken = token.substring(7);
        Long memberId = jwtTokenProvider.getMemberIdFromToken(jwtToken);

        boolean isFavorited = boardService.addFavoriteBoard(memberId, boardId);
        String message = isFavorited ? "즐겨찾기에 추가되었습니다." : "즐겨찾기 추가 실패.";
        return ResponseEntity.ok(ApiResponse.onSuccess(message));
    }

    @PostMapping("/{boardId}/favorite/remove")
    public ResponseEntity<ApiResponse<String>> removeFavoriteBoard(@RequestHeader("Authorization") String token,
                                                                   @PathVariable Long boardId) {
        String jwtToken = token.substring(7);
        Long memberId = jwtTokenProvider.getMemberIdFromToken(jwtToken);

        boolean isUnfavorited = boardService.removeFavoriteBoard(memberId, boardId);
        String message = isUnfavorited ? "즐겨찾기에서 제거되었습니다." : "즐겨찾기 제거 실패.";
        return ResponseEntity.ok(ApiResponse.onSuccess(message));
    }
}
