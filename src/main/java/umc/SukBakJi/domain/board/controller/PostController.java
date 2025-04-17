package umc.SukBakJi.domain.board.controller;

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

@RestController
@RequestMapping("/api/posts")
public class PostController {

    private final PostService postService;
    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    public PostController(PostService postService, JwtTokenProvider jwtTokenProvider) {
        this.postService = postService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<?>> createPost(@RequestBody CreatePostRequestDTO request, @RequestHeader("Authorization") String token) {
        try {
            token = token.replace("Bearer ", ""); // Remove "Bearer " prefix if present
            Long memberId = jwtTokenProvider.getMemberIdFromToken(token);
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

    @PostMapping("/createJobPost")
    public ResponseEntity<ApiResponse<?>> createJobPost(@RequestBody CreateJobPostRequestDTO request, @RequestHeader("Authorization") String token) {
        try {
            token = token.replace("Bearer ", ""); // Remove "Bearer " prefix if present
            Long memberId = jwtTokenProvider.getMemberIdFromToken(token);
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

    @PutMapping("/{postId}/update")
    public ResponseEntity<ApiResponse<?>> updatePost(
            @PathVariable Long postId,
            @RequestBody UpdatePostRequestDTO request,
            @RequestHeader("Authorization") String token) {
        try {
            token = token.replace("Bearer ", ""); // Remove "Bearer " prefix if present
            Long memberId = jwtTokenProvider.getMemberIdFromToken(token);
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

    @DeleteMapping("/{postId}/delete")
    public ResponseEntity<ApiResponse<?>> deletePost(
            @PathVariable Long postId,
            @RequestHeader("Authorization") String token) {
        try {
            token = token.replace("Bearer ", "");
            Long memberId = jwtTokenProvider.getMemberIdFromToken(token);
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
