package umc.SukBakJi.domain.board.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import umc.SukBakJi.domain.board.model.dto.*;
import umc.SukBakJi.domain.board.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import umc.SukBakJi.global.apiPayload.ApiResponse;
import umc.SukBakJi.global.apiPayload.exception.GeneralException;
import umc.SukBakJi.global.security.PrincipalDetails;
import umc.SukBakJi.global.security.jwt.JwtTokenProvider;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Tag(name = "게시글 API", description = "게시글 작성, 조회, 수정, 삭제 기능 제공")
@RestController
@RequestMapping("/api/posts")
public class PostController {

    private final PostService postService;

    @Autowired
    public PostController(PostService postService) {
        this.postService = postService;
    }

    @Operation(summary = "일반 게시글 작성", description = "로그인한 사용자가 게시글을 작성합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "게시글 작성 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "입력값 오류 또는 비속어 포함"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @PostMapping("/create")
    public ResponseEntity<ApiResponse<?>> createPost(@RequestBody CreatePostRequestDTO request,
                                                     @AuthenticationPrincipal PrincipalDetails principalDetails) {
        try {
            Long memberId = principalDetails.getMember().getId();

            PostResponseDTO createdPost = postService.createPost(request, memberId);
            return ResponseEntity.ok(ApiResponse.onSuccess("게시글 작성에 성공했습니다.", createdPost));
        } catch (GeneralException e) {
            return ResponseEntity.status(e.getErrorReasonHttpStatus().getHttpStatus())
                    .body(ApiResponse.onFailure(e.getErrorReasonHttpStatus().getCode(), e.getErrorReasonHttpStatus().getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(ApiResponse.onFailure("COMMON500", "Internal server error", null));
        }
    }

    @Operation(summary = "잡 게시글 작성", description = "로그인한 사용자가 잡 게시판에 게시글을 작성합니다.")
    @PostMapping("/createJobPost")
    public ResponseEntity<ApiResponse<?>> createJobPost(@RequestBody CreateJobPostRequestDTO request,
                                                        @AuthenticationPrincipal PrincipalDetails principalDetails) {
        try {
            Long memberId = principalDetails.getMember().getId();

            PostResponseDTO createdJobPost = postService.createJobPost(request, memberId);
            return ResponseEntity.ok(ApiResponse.onSuccess("잡 포스트 작성에 성공했습니다.", createdJobPost));
        } catch (GeneralException e) {
            return ResponseEntity.status(e.getErrorReasonHttpStatus().getHttpStatus())
                    .body(ApiResponse.onFailure(e.getErrorReasonHttpStatus().getCode(), e.getErrorReasonHttpStatus().getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(ApiResponse.onFailure("COMMON500", "Internal server error", null));
        }
    }

    @Operation(summary = "게시글 목록 조회", description = "메뉴와 게시판 이름에 따라 게시글 목록을 조회합니다.")
    @GetMapping("/list")
    public ResponseEntity<ApiResponse<List<PostListResponseDTO>>> getPostList(@RequestParam String menu,
                                                                              @RequestParam String boardName,
                                                                              @AuthenticationPrincipal PrincipalDetails principalDetails) {
        try {
            Long userId = principalDetails.getMember().getMemberId();
            List<PostListResponseDTO> postList = postService.getPostList(menu, boardName, userId);
            return ResponseEntity.ok(ApiResponse.onSuccess(postList));
        } catch (GeneralException e) {
            return ResponseEntity.status(e.getErrorReasonHttpStatus().getHttpStatus())
                    .body(ApiResponse.onFailure(e.getErrorReasonHttpStatus().getCode(), e.getErrorReasonHttpStatus().getMessage(), null));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(400)
                    .body(ApiResponse.onFailure("COMMON400", "Invalid menu or board name", null));
        }
    }

    @Operation(summary = "게시글 상세 조회", description = "게시글 ID를 통해 상세 내용을 조회합니다.")
    @GetMapping("/{postId}")
    public ResponseEntity<?> viewPostDetail(@PathVariable Long postId,
                                            @AuthenticationPrincipal PrincipalDetails principalDetails
                                            ) {
        try {
            Long userId = principalDetails.getMember().getMemberId();
            PostDetailResponseDTO postDetail = postService.getPostDetail(postId, userId);
            return ResponseEntity.ok(ApiResponse.onSuccess(postDetail));
        } catch (GeneralException e) {
            return ResponseEntity.status(e.getErrorReasonHttpStatus().getHttpStatus())
                    .body(ApiResponse.onFailure(e.getErrorReasonHttpStatus().getCode(), e.getErrorReasonHttpStatus().getMessage(), null));
        }
    }

    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    @Operation(summary = "게시글 수정", description = "자신이 작성한 게시글을 수정합니다.")
    @PutMapping("/{postId}/update")
    public ResponseEntity<ApiResponse<?>> updatePost(
            @PathVariable Long postId,
            @RequestBody UpdatePostRequestDTO request,
            @AuthenticationPrincipal PrincipalDetails principalDetails) {
        try {
            Long memberId = principalDetails.getMember().getId();

            PostResponseDTO updatedPost = postService.updatePost(postId, request, memberId);
            return ResponseEntity.ok(ApiResponse.onSuccess("게시글 수정에 성공했습니다.", updatedPost));
        } catch (GeneralException e) {
            return ResponseEntity.status(e.getErrorReasonHttpStatus().getHttpStatus())
                    .body(ApiResponse.onFailure(e.getErrorReasonHttpStatus().getCode(), e.getErrorReasonHttpStatus().getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(ApiResponse.onFailure("COMMON500", "Internal server error", null));
        }
    }

    @Operation(summary = "게시글 삭제", description = "자신이 작성한 게시글을 삭제합니다.")
    @DeleteMapping("/{postId}/delete")
    public ResponseEntity<ApiResponse<?>> deletePost(
            @PathVariable Long postId,
            @AuthenticationPrincipal PrincipalDetails principalDetails) {
        try {
            Long memberId = principalDetails.getMember().getId();

            postService.deletePost(postId, memberId);
            return ResponseEntity.ok(ApiResponse.onSuccess("게시글 삭제에 성공했습니다.", null));
        } catch (GeneralException e) {
            return ResponseEntity.status(e.getErrorReasonHttpStatus().getHttpStatus())
                    .body(ApiResponse.onFailure(e.getErrorReasonHttpStatus().getCode(), e.getErrorReasonHttpStatus().getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(ApiResponse.onFailure("COMMON500", "Internal server error", null));
        }
    }
}
