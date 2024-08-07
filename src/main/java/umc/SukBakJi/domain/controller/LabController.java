package umc.SukBakJi.domain.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import umc.SukBakJi.domain.service.LabService;
import umc.SukBakJi.global.apiPayload.ApiResponse;
import umc.SukBakJi.global.security.jwt.JwtTokenProvider;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/labs")
public class LabController {
    private final LabService labService;
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping("/{labId}/favorite")
    @Operation(summary = "연구실 즐겨찾기 추가", description = "특정 연구실을 사용자의 즐겨찾기에 추가합니다.")
    public ApiResponse<?> addFavoriteLab(@RequestHeader("Authorization") String token,
                                         @RequestParam Long labId) {
        String jwtToken = token.substring(7);
        Long userId = jwtTokenProvider.getMemberIdFromToken(jwtToken);
        labService.addFavoriteLab(userId, labId);
        return ApiResponse.onSuccess("연구실을 즐겨찾기에 추가하였습니다.");
    }

    @DeleteMapping("/{labId}/favorite")
    @Operation(summary = "연구실 즐겨찾기 취소", description = "특정 연구실을 사용자의 즐겨찾기에서 취소합니다.")
    public ApiResponse<?> cancelFavoriteLab(@RequestHeader("Authorization") String token,
                                            @RequestParam Long labId) {
        String jwtToken = token.substring(7);
        Long userId = jwtTokenProvider.getMemberIdFromToken(jwtToken);
        labService.cancelFavoriteLab(userId, labId);
        return ApiResponse.onSuccess("연구실을 즐겨찾기에서 취소하였습니다.");
    }
}
