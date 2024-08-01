package umc.SukBakJi.domain.controller;

import umc.SukBakJi.domain.model.dto.CommentResponseDTO;
import umc.SukBakJi.domain.model.dto.CreateCommentRequestDTO;
import umc.SukBakJi.domain.service.CommentService;
import umc.SukBakJi.global.apiPayload.ApiResponse;
import umc.SukBakJi.global.apiPayload.exception.GeneralException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/comments")
public class CommentController {

    private final CommentService commentService;

    @Autowired
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<CommentResponseDTO>> createComment(@RequestBody CreateCommentRequestDTO request) {
        try {
            CommentResponseDTO createdComment = commentService.createComment(request);
            return ResponseEntity.ok(ApiResponse.onSuccess(createdComment));
        } catch (GeneralException e) {
            return ResponseEntity.status(e.getErrorReasonHttpStatus().getHttpStatus())
                    .body(ApiResponse.onFailure(e.getErrorReasonHttpStatus().getCode(), e.getErrorReasonHttpStatus().getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(ApiResponse.onFailure("COMMON500", "Internal server error", null));
        }
    }
}
