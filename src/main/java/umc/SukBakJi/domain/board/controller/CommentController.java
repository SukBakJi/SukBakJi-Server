package umc.SukBakJi.domain.board.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Lombok;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import umc.SukBakJi.domain.board.model.dto.CommentResponseDTO;
import umc.SukBakJi.domain.board.model.dto.CreateCommentRequestDTO;
import umc.SukBakJi.domain.board.model.dto.UpdateCommentRequestDTO;
import umc.SukBakJi.domain.board.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import umc.SukBakJi.global.apiPayload.ApiResponse;
import umc.SukBakJi.global.apiPayload.exception.GeneralException;
import umc.SukBakJi.global.security.PrincipalDetails;
import umc.SukBakJi.global.security.jwt.JwtTokenProvider;

@Tag(name = "댓글 API", description = "댓글 생성, 수정 관련 API")
@RestController
@RequestMapping("/api/comments")
public class CommentController {

    private final CommentService commentService;

    @Autowired
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @Operation(summary = "댓글 작성", description = "게시글에 댓글을 작성합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "댓글 작성 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "입력 값 오류"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @PostMapping("/create")
    public ResponseEntity<ApiResponse<?>> createComment(@RequestBody CreateCommentRequestDTO request,
                                                        @AuthenticationPrincipal PrincipalDetails principalDetails) {
        try {
            Long memberId = principalDetails.getMember().getMemberId();
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

    @Operation(summary = "댓글 수정", description = "본인이 작성한 댓글을 수정합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "댓글 수정 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "권한 없음"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "댓글 없음"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @PutMapping("/update")
    public ResponseEntity<ApiResponse<?>> updateComment(@RequestBody UpdateCommentRequestDTO request,
                                                        @AuthenticationPrincipal PrincipalDetails principalDetails) {
        try {
            Long memberId = principalDetails.getMember().getMemberId();
            CommentResponseDTO updatedComment = commentService.updateComment(request, memberId);
            return ResponseEntity.ok(ApiResponse.onSuccess("댓글 수정에 성공했습니다.", updatedComment));
        } catch (GeneralException e) {
            return ResponseEntity.status(e.getErrorReasonHttpStatus().getHttpStatus())
                    .body(ApiResponse.onFailure(e.getErrorReasonHttpStatus().getCode(), e.getErrorReasonHttpStatus().getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(ApiResponse.onFailure("COMMON500", "Internal server error", null));
        }
    }
}


