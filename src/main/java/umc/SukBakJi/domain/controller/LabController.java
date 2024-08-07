package umc.SukBakJi.domain.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import umc.SukBakJi.domain.model.dto.InterestTopicsDTO;
import umc.SukBakJi.domain.model.dto.LabDetailResponseDTO;
import umc.SukBakJi.domain.model.dto.LabResponseDTO;
import umc.SukBakJi.domain.model.dto.SearchLabRequestDTO;
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
@RequestMapping("/api/labs")
public class LabController {

    private final LabService labService;

    @Autowired
    public LabController(LabService labService) {
        this.labService = labService;
    }

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private MemberRepository memberRepository;

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
}