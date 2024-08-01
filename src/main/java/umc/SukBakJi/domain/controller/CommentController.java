package umc.SukBakJi.domain.controller;

import umc.SukBakJi.domain.model.dto.CommentResponseDTO;
import umc.SukBakJi.domain.model.dto.CreateCommentRequestDTO;
import umc.SukBakJi.domain.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import umc.SukBakJi.global.apiPayload.ApiResponse;
import umc.SukBakJi.global.apiPayload.exception.GeneralException;
import umc.SukBakJi.global.security.jwt.JwtTokenProvider;

@RestController
@RequestMapping("/api/comments")
public class CommentController {

    private final CommentService commentService;
    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    public CommentController(CommentService commentService, JwtTokenProvider jwtTokenProvider) {
        this.commentService = commentService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<?>> createComment(@RequestBody CreateCommentRequestDTO request, @RequestHeader("Authorization") String token) {
        try {
            token = token.replace("Bearer ", ""); // Remove "Bearer " prefix if present
            Long memberId = jwtTokenProvider.getMemberIdFromToken(token);
            CommentResponseDTO createdComment = commentService.createComment(request, memberId);
            return ResponseEntity.ok(ApiResponse.onSuccess("댓글 작성에 성공했습니다.", createdComment));
        } catch (GeneralException e) {
            return ResponseEntity.status(e.getErrorReasonHttpStatus().getHttpStatus())
                    .body(ApiResponse.onFailure(e.getErrorReasonHttpStatus().getCode(), e.getErrorReasonHttpStatus().getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(ApiResponse.onFailure("COMMON500", "Internal server error", null));
        }
    }
}
