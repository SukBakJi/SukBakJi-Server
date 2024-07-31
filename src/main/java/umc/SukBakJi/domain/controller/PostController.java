package umc.SukBakJi.domain.controller;

import umc.SukBakJi.domain.model.dto.*;
import umc.SukBakJi.domain.model.entity.Post;
import umc.SukBakJi.domain.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import umc.SukBakJi.global.apiPayload.ApiResponse;

import jakarta.validation.Valid;
import umc.SukBakJi.global.apiPayload.ApiResponse;
import umc.SukBakJi.global.apiPayload.exception.GeneralException;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    private final PostService postService;

    @Autowired
    public PostController(PostService postService) {
        this.postService = postService;
    }

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<?>> createPost(@RequestBody CreatePostRequestDTO request, @RequestParam Long memberId) {
        try {
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
    public ResponseEntity<ApiResponse<?>> createJobPost(@RequestBody CreateJobPostRequestDTO request, @RequestParam Long memberId) {
        try {
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
    public ResponseEntity<ApiResponse<List<PostListResponseDTO>>> getPostList(@RequestParam String menu, @RequestParam String boardName) {
        try {
            List<PostListResponseDTO> postList = postService.getPostList(menu, boardName);
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
    public ResponseEntity<?> viewPostDetail(@PathVariable Long postId) {
        try {
            PostDetailResponseDTO postDetail = postService.getPostDetail(postId);
            return ResponseEntity.ok(ApiResponse.onSuccess(postDetail));
        } catch (GeneralException e) {
            return ResponseEntity.status(e.getErrorReasonHttpStatus().getHttpStatus())
                    .body(ApiResponse.onFailure(e.getErrorReasonHttpStatus().getCode(), e.getErrorReasonHttpStatus().getMessage(), null));
        }
    }
}
