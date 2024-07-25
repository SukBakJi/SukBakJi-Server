package umc.SukBakJi.domain.controller;

import jakarta.validation.Valid;
import org.springframework.validation.BindingResult;
import umc.SukBakJi.domain.model.dto.CreateBoardRequest;
import umc.SukBakJi.domain.model.entity.Board;
import umc.SukBakJi.domain.model.entity.enums.Menu;
import umc.SukBakJi.domain.service.BoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/boards")
public class BoardController {
    private final BoardService boardService;

    @Autowired
    public BoardController(BoardService boardService) {
        this.boardService = boardService;
    }

    @GetMapping("/{menu}")
    public ResponseEntity<List<String>> getBoardNamesByMenu(@PathVariable Menu menu) {
        List<String> boardNames = boardService.getBoardNamesByMenu(menu);
        return ResponseEntity.ok(boardNames);
    }

    @PostMapping("/create")
    public ResponseEntity<?> createBoard(@Valid @RequestBody CreateBoardRequest request, BindingResult result) {
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
}

