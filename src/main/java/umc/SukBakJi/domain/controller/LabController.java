package umc.SukBakJi.domain.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import umc.SukBakJi.domain.model.dto.*;
import umc.SukBakJi.domain.model.entity.Member;
import umc.SukBakJi.domain.repository.MemberRepository;
import umc.SukBakJi.domain.service.LabService;
import umc.SukBakJi.global.apiPayload.ApiResponse;
import umc.SukBakJi.global.apiPayload.code.status.ErrorStatus;
import umc.SukBakJi.global.apiPayload.exception.GeneralException;
import umc.SukBakJi.global.apiPayload.exception.handler.MemberHandler;
import umc.SukBakJi.global.security.jwt.JwtTokenProvider;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/labs")
public class LabController {
    private final LabService labService;
    private final JwtTokenProvider jwtTokenProvider;
    private final MemberRepository memberRepository;

    @PostMapping("/search")
    public ResponseEntity<ApiResponse<List<LabResponseDTO>>> searchLabs(@RequestBody SearchLabRequestDTO searchRequest) {
        List<LabResponseDTO> labs = labService.searchLabsByTopicName(searchRequest.getTopicName());
        return ResponseEntity.ok(ApiResponse.onSuccess(labs));
    }

    @GetMapping("/{labId}")
    public ResponseEntity<ApiResponse<LabDetailResponseDTO>> getLabDetail(@PathVariable Long labId) {
        try {
            LabDetailResponseDTO labDetail = labService.getLabDetail(labId);
            return ResponseEntity.ok(ApiResponse.onSuccess(labDetail));
        } catch (GeneralException e) {
            return ResponseEntity.status(e.getErrorReasonHttpStatus().getHttpStatus())
                    .body(ApiResponse.onFailure(e.getErrorReasonHttpStatus().getCode(), e.getErrorReasonHttpStatus().getMessage(), null));
        }
    }

    @Operation(summary = "사용자의 관심 연구 주제 목록 조회", description = "사용자가 설정한 관심 주제 목록을 조회합니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없습니다."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "서버 에러, 관리자에게 문의 바랍니다.")
    })
    @GetMapping("/mypage/interest-topics")
    public ResponseEntity<ApiResponse<InterestTopicsDTO>> getInterestTopicDTO(@RequestHeader("Authorization") String token) {

        String jwt = token.substring(7);

        String email = jwtTokenProvider.getEmailFromToken(jwt);
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));

        InterestTopicsDTO topics = labService.getInterestTopics(member);

        return ResponseEntity.ok(ApiResponse.onSuccess(topics));
    }

    @Operation(summary = "사용자의 즐겨찾기 연구실 목록 조회", description = "사용자가 즐겨찾기한 연구실 목록을 조회합니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없습니다."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "서버 에러, 관리자에게 문의 바랍니다.")
    })
    @GetMapping("/mypage/favorite-labs")
    public ApiResponse<List<LabResponseDTO>> getLabInfo(@RequestHeader("Authorization") String token) {
        String jwtToken = token.substring(7);
        Long memberId = jwtTokenProvider.getMemberIdFromToken(jwtToken);

        List<LabResponseDTO> favoriteLabs = labService.getFavoriteLabs(memberId);
        return ApiResponse.onSuccess(favoriteLabs);
    }

    @PostMapping("/{labId}/favorite")
    @Operation(summary = "연구실 즐겨찾기 추가 및 취소",
                description = "특정 연구실을 사용자의 즐겨찾기에 추가하고, 이미 추가한 상태라면 다시 요청했을 때 즐겨찾기를 취소합니다.")
    public ApiResponse<?> toggleFavoriteLab(@RequestHeader("Authorization") String token,
                                         @RequestParam Long labId) {
        String jwtToken = token.substring(7);
        Long memberId = jwtTokenProvider.getMemberIdFromToken(jwtToken);
        Boolean isAdded = labService.toggleFavoriteLab(memberId, labId);
        String message = isAdded ? "연구실을 즐겨찾기에 추가하였습니다." : "연구실을 즐겨찾기에서 취소하였습니다.";
        return ApiResponse.onSuccess(message);
    }

    @PostMapping("/{labId}/cancel-favorite")
    @Operation(summary = "연구실 즐겨찾기 항목 취소",
            description = "즐겨찾기에서 삭제할 연구실 ID를 입력해 즐겨찾기를 취소합니다.")
    public ApiResponse<?> cancelFavoriteLab(@RequestHeader("Authorization") String token,
                                            @RequestBody LabRequestDTO.CancelLabDTO request) {
        String jwtToken = token.substring(7);
        Long memberId = jwtTokenProvider.getMemberIdFromToken(jwtToken);
        labService.cancelFavoriteLab(memberId, request);
        return ApiResponse.onSuccess("연구실을 즐겨찾기에서 취소하였습니다.");
    }
}