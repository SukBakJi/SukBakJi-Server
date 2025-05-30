package umc.SukBakJi.domain.lab.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import umc.SukBakJi.domain.lab.model.dto.*;
import umc.SukBakJi.global.apiPayload.ApiResponse;
import umc.SukBakJi.domain.lab.service.LabReviewService;
import umc.SukBakJi.global.apiPayload.code.ErrorReasonDTO;
import umc.SukBakJi.global.security.PrincipalDetails;
import umc.SukBakJi.global.security.jwt.JwtTokenProvider;

import java.util.List;
import java.util.Optional;

@Tag(name = "연구실 후기 API", description = "연구실 후기 작성, 조회, 검색 및 문의 관련 기능 제공")
@RestController
@RequestMapping("/api/labs/reviews")
public class LabReviewController {
    @Autowired
    private LabReviewService labReviewService;

    @Operation(summary = "연구실 별 후기 상세 조회", description = "연구실 후기 상세 정보를 조회합니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "성공",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = LabReviewDetailsDTO.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "연구실 후기를 찾을 수 없습니다.",
                    content = @Content),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "서버 에러, 관리자에게 문의 바랍니다.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorReasonDTO.class)))
    })
    @GetMapping("/{lab_id}")
    public ApiResponse<LabReviewSummaryDTO> getLabReviewDetails(@PathVariable("lab_id") Long labId) {
        LabReviewSummaryDTO summaryDTO = labReviewService.getLabReviews(labId);
        return ApiResponse.onSuccess(summaryDTO);
    }

    @Operation(summary = "연구실 후기 작성", description = "연구실 후기를 작성합니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "성공",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = LabReviewDetailsDTO.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "4041", description = "연구실을 찾을 수 없습니다.",
                    content = @Content),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "409", description = "중복된 후기가 존재합니다.",
                    content = @Content),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "4042", description = "후기를 작성할 사용자를 찾을 수 없습니다.",
                    content = @Content),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "후기 내용이 유효하지 않습니다.",
                    content = @Content),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "서버 에러, 관리자에게 문의 바랍니다.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorReasonDTO.class)))
    })
    @PostMapping("/{lab_id}")
    public ApiResponse<LabReviewDetailsDTO> createLabReview(
            @Parameter(description = "연구실 후기 생성 DTO", required = true)
            @PathVariable("lab_id") Long labId,
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @RequestBody LabReviewCreateDTO dto) {

        Long userId = principalDetails.getMember().getMemberId();

        LabReviewDetailsDTO details = labReviewService.createLabReview(dto, labId, userId);
        return ApiResponse.onSuccess(details);
    }

    @Operation(summary = "모든 연구실의 후기 목록 조회", description = "연구실 후기 목록을 조회합니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "성공",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = LabReviewDetailsDTO.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "잘못된 요청입니다.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorReasonDTO.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "연구실 후기를 찾을 수 없습니다.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorReasonDTO.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "서버 에러, 관리자에게 문의 바랍니다.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorReasonDTO.class)))
    })
    @GetMapping
    public ApiResponse<List<LabReviewDetailsDTO>> getLabReviewList(
            @Parameter(description = "시작 위치", example = "0") @RequestParam(defaultValue = "0") int offset,
            @Parameter(description = "반환할 후기 개수") @RequestParam Optional<Integer> limit
    ) {
        List<LabReviewDetailsDTO> reviewList = labReviewService.getLabReviewList(offset, limit);
        return ApiResponse.onSuccess(reviewList);
    }

    @Operation(summary = "연구실 후기 검색", description = "지도교수명으로 연구실 후기를 검색합니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "성공",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = LabReviewDetailsDTO.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "해당 지도교수를 찾을 수 없습니다.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorReasonDTO.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "연구실 후기를 찾을 수 없습니다.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorReasonDTO.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "서버 에러, 관리자에게 문의 바랍니다.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorReasonDTO.class)))
    })
    @PostMapping("/search")
    public ApiResponse<List<LabReviewDetailsDTO>> searchLabReview(
            @RequestBody LabReviewSearchDTO searchDTO
    ) {
        List<LabReviewDetailsDTO> reviewList = labReviewService.searchLabReviews(searchDTO.getProfessorName());
        return ApiResponse.onSuccess(reviewList);
    }

    @PostMapping("/{labId}/inquiries")
    @Operation(summary = "연구실 후기 문의 등록",
            description = "연구실 후기에 잘못 기입되어 있는 정보에 대한 문의를 등록합니다.")
    public ApiResponse<String> inquiryLabInfo(@AuthenticationPrincipal PrincipalDetails principalDetails,
                                         @RequestBody LabRequestDTO.InquireLabReviewDTO request) {
        Long memberId = principalDetails.getMember().getMemberId();
        labReviewService.labReviewUpdateRequest(memberId, request);
        return ApiResponse.onSuccess("연구실 후기에 대한 문의를 등록하였습니다.");
    }
}
