package umc.SukBakJi.domain.board.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import umc.SukBakJi.domain.board.service.ScrapService;
import umc.SukBakJi.global.apiPayload.ApiResponse;
import umc.SukBakJi.global.security.PrincipalDetails;
import umc.SukBakJi.global.security.jwt.JwtTokenProvider;

@Tag(name = "스크랩 API", description = "게시글 스크랩 기능을 제공합니다.")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/scraps")
public class ScrapController {

    private final ScrapService scrapService;

    @Operation(summary = "스크랩 토글", description = "게시글을 스크랩하거나 스크랩을 해제합니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "스크랩 상태 변경 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "인증되지 않은 사용자"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @PostMapping("/{postId}/toggle")
    public ResponseEntity<ApiResponse<String>> toggleScrap(@AuthenticationPrincipal PrincipalDetails principalDetails,
                                                           @PathVariable Long postId) {
        Long memberId = principalDetails.getMember().getMemberId();

        boolean isScrapped = scrapService.toggleScrapPost(memberId, postId);
        String message = isScrapped ? "게시글이 스크랩되었습니다." : "게시글이 스크랩에서 제거되었습니다.";
        return ResponseEntity.ok(ApiResponse.onSuccess(message));
    }

    @Operation(summary = "스크랩 삭제", description = "특정 게시글의 스크랩을 삭제합니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "스크랩 삭제 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "인증되지 않은 사용자"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @DeleteMapping("/{postId}")
    public ResponseEntity<ApiResponse<String>> deleteScrap(@AuthenticationPrincipal PrincipalDetails principalDetails,
                                                           @PathVariable Long postId) {
        Long memberId = principalDetails.getMember().getMemberId();

        scrapService.deleteScrap(memberId, postId);
        return ResponseEntity.ok(ApiResponse.onSuccess("스크랩이 삭제되었습니다."));
    }
}
