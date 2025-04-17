package umc.SukBakJi.domain.board.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import umc.SukBakJi.domain.board.model.dto.HotBoardPostDTO;
import umc.SukBakJi.domain.board.model.dto.LatestQuestionDTO;
import umc.SukBakJi.domain.board.model.dto.PostListDTO;
import umc.SukBakJi.domain.board.model.dto.PostSearchDTO;
import umc.SukBakJi.domain.common.entity.enums.Menu;
import umc.SukBakJi.domain.board.service.CommunityService;
import umc.SukBakJi.global.apiPayload.ApiResponse;
import umc.SukBakJi.global.security.PrincipalDetails;

import java.util.List;

@Tag(name = "커뮤니티 API", description = "커뮤니티 기능 관련 API (질문글, HOT 게시판, 내가 쓴 글, 스크랩 등)")
@RestController
@RequestMapping("/api/community")
public class CommunityController {

    @Autowired
    private CommunityService communityService;

    @Operation(summary = "최근 질문글 3개 불러오기", description = "각 메뉴(박사, 석사, 진학예정)별로 질문게시판의 최신 질문글을 가져옵니다.")
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

    @Operation(summary = "Hot 게시판 글 목록 불러오기", description = "스크랩 20개 이상 또는 조회수 100회 이상인 게시글을 최신순으로 보여줍니다.")
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

    @Operation(summary = "내가 작성한 스크랩 게시글 목록", description = "사용자가 스크랩한 게시물 목록을 조회합니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "성공",
                    content = @io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json",
                            schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = PostListDTO.class)))
    })
    @GetMapping("/scrap-list")
    public ApiResponse<List<PostListDTO>> getScrapListByUserId(@AuthenticationPrincipal PrincipalDetails principalDetails) {
        Long userId = principalDetails.getMember().getId();

        List<PostListDTO> scrapList = communityService.getScrappedPostsByUserId(userId);
        return ApiResponse.onSuccess(scrapList);
    }

    @Operation(summary = "내가 작성한 게시글 목록", description = "사용자가 작성한 게시물 목록을 조회합니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "성공",
                    content = @io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json",
                            schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = PostListDTO.class)))
    })
    @GetMapping("/post-list")
    public ApiResponse<List<PostListDTO>> getPostListByUser(@AuthenticationPrincipal PrincipalDetails principalDetails) {
        Long userId = principalDetails.getMember().getId();

        List<PostListDTO> postList = communityService.getPostsByUserId(userId);
        return ApiResponse.onSuccess(postList);
    }

    @Operation(summary = "내가 작성한 댓글이 있는 게시글 목록", description = "사용자가 작성한 댓글이 포함된 게시물 목록을 조회합니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "성공",
                    content = @io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json",
                            schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = PostListDTO.class)))
    })
    @GetMapping("/comment-list")
    public ApiResponse<List<PostListDTO>> getCommentedPostListByUser(@AuthenticationPrincipal PrincipalDetails principalDetails) {
        Long userId = principalDetails.getMember().getId();

        List<PostListDTO> postList = communityService.getCommentedPostsByUserId(userId);
        return ApiResponse.onSuccess(postList);
    }

    @Operation(summary = "즐겨찾기한 게시판의 최근 게시글 1개씩 불러오기", description = "사용자가 즐겨찾기한 게시판의 최신 게시물 목록을 조회합니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "성공",
                    content = @io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json",
                            schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = PostListDTO.class)))
    })
    @GetMapping("/favorite-post-list")
    public ApiResponse<List<PostListDTO>> getFavoritePosts(@AuthenticationPrincipal PrincipalDetails principalDetails) {
        Long userId = principalDetails.getMember().getId();

        List<PostListDTO> favoritePosts = communityService.getFavoritePosts(userId);
        return ApiResponse.onSuccess(favoritePosts);
    }

    @Operation(summary = "게시글 검색", description = "키워드를 통해 게시글을 검색합니다. 특정 메뉴와 게시판 이름을 기준으로 검색하거나, 전체 게시판에서 검색할 수 있습니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "게시글을 찾을 수 없습니다."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "서버 에러, 관리자에게 문의 바랍니다.")
    })
    @GetMapping("/search")
    public ApiResponse<List<PostSearchDTO>> searchPosts(
            @RequestParam String keyword,
            @RequestParam(required = false) Menu menu, // 선택적 파라미터
            @RequestParam(required = false) String boardName // 선택적 파라미터
    ) {
        List<PostSearchDTO> postList = communityService.searchPosts(keyword, menu, boardName);
        return ApiResponse.onSuccess(postList);
    }
}
