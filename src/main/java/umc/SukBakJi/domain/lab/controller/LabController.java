package umc.SukBakJi.domain.lab.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import umc.SukBakJi.domain.lab.model.dto.InterestTopicsDTO;
import umc.SukBakJi.domain.lab.model.dto.LabDetailResponseDTO;
import umc.SukBakJi.domain.lab.model.dto.LabRequestDTO;
import umc.SukBakJi.domain.lab.model.dto.LabResponseDTO;
import umc.SukBakJi.domain.member.model.entity.Member;
import umc.SukBakJi.domain.member.repository.MemberRepository;
import umc.SukBakJi.domain.lab.service.LabService;
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
    public ResponseEntity<ApiResponse<LabResponseDTO.LabSearchResponseDTO>> searchLabs(
            @RequestParam String topicName,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "6") int size) {
        LabResponseDTO.LabSearchResponseDTO labs = labService.searchLabsByTopicName(topicName, page, size);
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

    @GetMapping("/labs/filter")
    @Operation(summary = "대학교에 따른 연구실 목록 필터링",
            description = "특정 대학에 해당하는 연구실 정보 리스트와 총 연구실 개수를 반환합니다.")
    public ApiResponse<LabResponseDTO.LabSearchResponseDTO> filterLabsByUniversity(
            @RequestParam Long univId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "6") int size
    ) {
        LabResponseDTO.LabSearchResponseDTO response = labService.filterLabsByUniversity(univId, page, size);
        return ApiResponse.onSuccess(response);
    }

    @GetMapping("/labs/filters/universities")
    @Operation(summary = "필터링 대학 목록 조회",
            description = "필터링이 가능한 대학 목록을 조회합니다.")
    public ApiResponse<List<LabResponseDTO.UniversityFilterResponseDTO>> getFilterableUniversities(
            @AuthenticationPrincipal Long memberId) {

        List<LabResponseDTO.UniversityFilterResponseDTO> universities = labService.getFilterableUniversities(memberId);
        return ApiResponse.onSuccess(universities);
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
    public ApiResponse<List<LabResponseDTO.LabPreviewResponseDTO>> getLabInfo(@RequestHeader("Authorization") String token) {
        String jwtToken = token.substring(7);
        Long memberId = jwtTokenProvider.getMemberIdFromToken(jwtToken);

        List<LabResponseDTO.LabPreviewResponseDTO> favoriteLabs = labService.getFavoriteLabs(memberId);
        return ApiResponse.onSuccess(favoriteLabs);
    }

    @PostMapping("/{labId}/favorite")
    @Operation(summary = "연구실 즐겨찾기 추가 및 취소",
                description = "특정 연구실을 사용자의 즐겨찾기에 추가하고, 이미 추가한 상태라면 다시 요청했을 때 즐겨찾기를 취소합니다.")
    public ApiResponse<String> toggleFavoriteLab(
            @AuthenticationPrincipal Long memberId,
            @RequestParam Long labId) {
        Boolean isAdded = labService.toggleFavoriteLab(memberId, labId);
        String message = isAdded ? "연구실을 즐겨찾기에 추가하였습니다." : "연구실을 즐겨찾기에서 취소하였습니다.";
        return ApiResponse.onSuccess(message);
    }

    @PostMapping("/{labId}/cancel-favorite")
    @Operation(summary = "연구실 즐겨찾기 항목 취소",
            description = "즐겨찾기에서 삭제할 연구실 ID를 입력해 즐겨찾기를 취소합니다.")
    public ApiResponse<String> cancelFavoriteLab(
            @AuthenticationPrincipal Long memberId,
            @RequestBody LabRequestDTO.CancelLabDTO request) {
        labService.cancelFavoriteLab(memberId, request);
        return ApiResponse.onSuccess("연구실을 즐겨찾기에서 삭제하였습니다.");
    }

    @PostMapping("/{labId}/inquiries")
    @Operation(summary = "연구실 정보 문의 등록",
            description = "연구실에 잘못 기입되어 있는 정보에 대한 문의를 등록합니다.")
    public ApiResponse<String> inquiryLabInfo(
            @AuthenticationPrincipal Long memberId,
            @RequestBody LabRequestDTO.InquireLabDTO request) {
        labService.labUpdateRequest(memberId, request);
        return ApiResponse.onSuccess("연구실 정보에 대한 문의를 등록하였습니다.");
    }
}