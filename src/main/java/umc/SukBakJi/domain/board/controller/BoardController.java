package umc.SukBakJi.domain.board.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import umc.SukBakJi.domain.board.model.dto.BoardLikeResponseDTO;
import umc.SukBakJi.domain.board.model.dto.CreateBoardRequestDTO;
import umc.SukBakJi.domain.board.model.entity.Board;
import umc.SukBakJi.domain.common.entity.enums.Menu;
import umc.SukBakJi.domain.board.service.BoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import umc.SukBakJi.global.apiPayload.ApiResponse;
import umc.SukBakJi.global.security.PrincipalDetails;

import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "게시판 API", description = "게시판 생성, 즐겨찾기 등 관련 기능 제공")
@RestController
@RequestMapping("/api/boards")
public class BoardController {
    private final BoardService boardService;

    @Autowired
    public BoardController(BoardService boardService) {
        this.boardService = boardService;
    }
    @Operation(summary = "메뉴별 게시판 이름 조회", description = "특정 메뉴에 속한 게시판들의 이름 목록을 조회합니다.")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "게시판 이름 목록 조회 성공")
    @GetMapping("/{menu}")
    public ResponseEntity<List<String>> getBoardNamesByMenu(@PathVariable Menu menu) {
        List<String> boardNames = boardService.getBoardNamesByMenu(menu);
        return ResponseEntity.ok(boardNames);
    }

    @Operation(summary = "게시판 생성", description = "자유 메뉴에 새로운 게시판을 생성합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "게시판 생성 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "중복된 게시판명 또는 입력 오류"),
    })
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

    @Operation(summary = "게시판 즐겨찾기 추가", description = "현재 로그인한 사용자가 특정 게시판을 즐겨찾기에 추가합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "즐겨찾기 추가 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "이미 즐겨찾기된 게시판"),
    })
    @PostMapping("/{boardId}/favorite/add")
    public ResponseEntity<ApiResponse<String>> addFavoriteBoard(@AuthenticationPrincipal PrincipalDetails principalDetails,
                                                                @PathVariable Long boardId) {
        Long memberId = principalDetails.getMember().getMemberId();

        boolean isFavorited = boardService.addFavoriteBoard(memberId, boardId);
        String message = isFavorited ? "즐겨찾기에 추가되었습니다." : "즐겨찾기 추가 실패.";
        return ResponseEntity.ok(ApiResponse.onSuccess(message));
    }

    @Operation(summary = "게시판 즐겨찾기 제거", description = "현재 로그인한 사용자가 특정 게시판을 즐겨찾기에서 제거합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "즐겨찾기 제거 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "즐겨찾기 항목이 존재하지 않음"),
    })
    @PostMapping("/{boardId}/favorite/remove")
    public ResponseEntity<ApiResponse<String>> removeFavoriteBoard(@AuthenticationPrincipal PrincipalDetails principalDetails,
                                                                   @PathVariable Long boardId) {
        Long memberId = principalDetails.getMember().getMemberId();

        boolean isUnfavorited = boardService.removeFavoriteBoard(memberId, boardId);
        String message = isUnfavorited ? "즐겨찾기에서 제거되었습니다." : "즐겨찾기 제거 실패.";
        return ResponseEntity.ok(ApiResponse.onSuccess(message));
    }

    @Operation(summary = "게시판 즐겨찾기 목록 조회", description = "사용자가 즐겨찾기한 게시판 목록을 조회합니다.")
    @GetMapping("/favorite")
    public ResponseEntity<ApiResponse<List<BoardLikeResponseDTO>>> getFavoriteBoardList(@AuthenticationPrincipal PrincipalDetails principalDetails) {
        Long memberId = principalDetails.getMember().getMemberId();
        List<BoardLikeResponseDTO> boardLikeList = boardService.getFavoriteBoardList(memberId);
        return ResponseEntity.ok(ApiResponse.onSuccess(boardLikeList));
    }
}
