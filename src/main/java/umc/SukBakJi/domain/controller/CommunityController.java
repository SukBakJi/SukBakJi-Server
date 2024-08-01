package umc.SukBakJi.domain.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import umc.SukBakJi.domain.model.dto.HotBoardPostDTO;
import umc.SukBakJi.domain.model.dto.LatestQuestionDTO;
import umc.SukBakJi.domain.model.dto.PostListDTO;
import umc.SukBakJi.domain.model.entity.Member;
import umc.SukBakJi.domain.repository.MemberRepository;
import umc.SukBakJi.domain.service.CommunityService;
import umc.SukBakJi.global.apiPayload.ApiResponse;
import umc.SukBakJi.global.apiPayload.code.status.ErrorStatus;
import umc.SukBakJi.global.apiPayload.exception.handler.MemberHandler;
import umc.SukBakJi.global.security.jwt.JwtTokenProvider;

import java.util.List;
import java.util.logging.Logger;

import static org.hibernate.query.sqm.tree.SqmNode.log;

@RestController
@RequestMapping("/api/community")
public class CommunityController {

    @Autowired
    private CommunityService communityService;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Operation(summary = "Get Latest Questions", description = "각 메뉴(박사, 석사, 진학예정)별로 질문게시판의 최신 질문글을 가져옵니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "성공",
                    content = @io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json",
                            schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = LatestQuestionDTO.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "해당 메뉴에 질문글이 없습니다.")
    })
    @GetMapping("/latest-questions")
    public ApiResponse<List<LatestQuestionDTO>> getLatestQuestions() {
        List<LatestQuestionDTO> latestQuestions = communityService.getLatestQuestions();
        return ApiResponse.onSuccess(latestQuestions);
    }

    @Operation(summary = "Get Hot Board Posts", description = "스크랩 20개 이상 또는 조회수 100회 이상인 게시글을 최신순으로 보여줍니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "성공",
                    content = @io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json",
                            schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = HotBoardPostDTO.class)))
    })
    @GetMapping("/hot-boards")
    public ApiResponse<List<HotBoardPostDTO>> getHotBoardPosts() {
        List<HotBoardPostDTO> hotBoardPosts = communityService.getHotBoardPosts();
        return ApiResponse.onSuccess(hotBoardPosts);
    }

    @Operation(summary = "Get Scrap List", description = "사용자가 스크랩한 게시물 목록을 조회합니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "성공",
                    content = @io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json",
                            schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = PostListDTO.class)))
    })
    @GetMapping("/scrap-list")
    public ApiResponse<List<PostListDTO>> getScrapListByUserId(@RequestHeader("Authorization") String token) {
        String jwtToken = token.substring(7);
        Long userId = jwtTokenProvider.getMemberIdFromToken(jwtToken);

        List<PostListDTO> scrapList = communityService.getScrappedPostsByUserId(userId);
        return ApiResponse.onSuccess(scrapList);
    }

    @Operation(summary = "Get Post List", description = "사용자가 작성한 게시물 목록을 조회합니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "성공",
                    content = @io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json",
                            schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = PostListDTO.class)))
    })
    @GetMapping("/post-list")
    public ApiResponse<List<PostListDTO>> getPostListByUser(@RequestHeader("Authorization") String token) {
        String jwtToken = token.substring(7);
        Long userId = jwtTokenProvider.getMemberIdFromToken(jwtToken);

        List<PostListDTO> postList = communityService.getPostsByUserId(userId);
        return ApiResponse.onSuccess(postList);
    }

    @Operation(summary = "Get Comment Post List", description = "사용자가 작성한 게시물 목록을 조회합니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "성공",
                    content = @io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json",
                            schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = PostListDTO.class)))
    })
    @GetMapping("/comment-list")
    public ApiResponse<List<PostListDTO>> getCommentedPostListByUser(@RequestHeader("Authorization") String token) {
        String jwtToken = token.substring(7);
        Long userId = jwtTokenProvider.getMemberIdFromToken(jwtToken);

        List<PostListDTO> postList = communityService.getCommentedPostsByUserId(userId);
        return ApiResponse.onSuccess(postList);
    }

    @Operation(summary = "Get Favorite Board List", description = "사용자가 즐겨찾기한 게시판의 최신 게시물 목록을 조회합니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "성공",
                    content = @io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json",
                            schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = PostListDTO.class)))
    })
    @GetMapping("/favorite-post-list")
    public ApiResponse<List<PostListDTO>> getFavoritePosts(@RequestHeader("Authorization") String token) {
        String jwtToken = token.substring(7);

        Long userId = jwtTokenProvider.getMemberIdFromToken(jwtToken);
        List<PostListDTO> favoritePosts = communityService.getFavoritePosts(userId);
        return ApiResponse.onSuccess(favoritePosts);
    }
}
