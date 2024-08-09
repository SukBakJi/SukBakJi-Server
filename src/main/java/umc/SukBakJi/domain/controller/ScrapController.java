package umc.SukBakJi.domain.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import umc.SukBakJi.domain.service.ScrapService;
import umc.SukBakJi.global.apiPayload.ApiResponse;
import umc.SukBakJi.global.security.jwt.JwtTokenProvider;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/scraps")
public class ScrapController {

    private final ScrapService scrapService;
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping("/{postId}/toggle")
    public ResponseEntity<ApiResponse<String>> toggleScrap(@RequestHeader("Authorization") String token,
                                                           @PathVariable Long postId) {
        String jwtToken = token.substring(7);
        Long memberId = jwtTokenProvider.getMemberIdFromToken(jwtToken);

        boolean isScrapped = scrapService.toggleScrapPost(memberId, postId);
        String message = isScrapped ? "게시글이 스크랩되었습니다." : "게시글이 스크랩에서 제거되었습니다.";
        return ResponseEntity.ok(ApiResponse.onSuccess(message));
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<ApiResponse<String>> deleteScrap(@RequestHeader("Authorization") String token,
                                                           @PathVariable Long postId) {
        String jwtToken = token.substring(7);
        Long memberId = jwtTokenProvider.getMemberIdFromToken(jwtToken);

        scrapService.deleteScrap(memberId, postId);
        return ResponseEntity.ok(ApiResponse.onSuccess("스크랩이 삭제되었습니다."));
    }
}
