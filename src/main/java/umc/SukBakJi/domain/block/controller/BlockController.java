package umc.SukBakJi.domain.block.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import umc.SukBakJi.domain.block.model.dto.BlockResponseDto;
import umc.SukBakJi.domain.block.service.BlockService;
import umc.SukBakJi.global.apiPayload.ApiResponse;
import umc.SukBakJi.global.security.PrincipalDetails;

@Tag(name = "차단 API", description = "유저 차단 기능 (게시글/댓글 작성자 차단)")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/block")
public class BlockController {

    private final BlockService blockService;

    @Operation(summary = "유저 차단", description = "게시글 또는 댓글 작성자를 차단합니다. 차단된 유저의 글/댓글은 차단한 사용자에게 표시되지 않습니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "차단 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "잘못된 요청 또는 이미 차단된 사용자"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "차단 대상이 존재하지 않음")
    })
    @PostMapping("/{targetMemberId}")
    public ApiResponse<BlockResponseDto> blockUser(@PathVariable Long targetMemberId,
                                         @AuthenticationPrincipal PrincipalDetails principalDetails) {
        Long blockerId = principalDetails.getMember().getMemberId();
        blockService.blockMember(blockerId, targetMemberId);
        return ApiResponse.onSuccess(new BlockResponseDto(blockerId, targetMemberId, true));
    }
}

