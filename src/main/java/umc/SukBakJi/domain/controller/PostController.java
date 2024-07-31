package umc.SukBakJi.domain.controller;

import umc.SukBakJi.domain.model.dto.CreateJobPostRequestDTO;
import umc.SukBakJi.domain.model.dto.CreatePostRequestDTO;
import umc.SukBakJi.domain.model.dto.PostDetailResponseDTO;
import umc.SukBakJi.domain.model.dto.PostListResponseDTO;
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
    public ResponseEntity<?> createPost(@Valid @RequestBody CreatePostRequestDTO request, BindingResult result,
                                        @RequestParam Long memberId) {
        if (result.hasErrors()) {
            String errors = result.getAllErrors().stream()
                    .map(error -> error.getDefaultMessage())
                    .collect(Collectors.joining(", "));
            return ResponseEntity.badRequest().body(errors);
        }

        try {
            Post createdPost = postService.createPost(request, memberId);
            return ResponseEntity.ok(createdPost);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/create-job-post")
    public ResponseEntity<?> createJobPost(@Valid @RequestBody CreateJobPostRequestDTO request, BindingResult result,
                                           @RequestParam Long memberId) {
        if (result.hasErrors()) {
            String errors = result.getAllErrors().stream()
                    .map(error -> error.getDefaultMessage())
                    .collect(Collectors.joining(", "));
            return ResponseEntity.badRequest().body(errors);
        }

        try {
            Post createdPost = postService.createJobPost(request, memberId);
            return ResponseEntity.ok(createdPost);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/list")
    public ResponseEntity<List<PostListResponseDTO>> getPostList(@RequestParam String menu, @RequestParam String boardName) {
        try {
            List<PostListResponseDTO> postList = postService.getPostList(menu, boardName);
            return ResponseEntity.ok(postList);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/{postId}")
    public ResponseEntity<PostDetailResponseDTO> getPostDetails(@PathVariable Long postId) {
        PostDetailResponseDTO postDetails = postService.getPostDetails(postId);
        return ResponseEntity.ok(postDetails);
    }
}
