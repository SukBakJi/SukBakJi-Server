package umc.SukBakJi.domain.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import umc.SukBakJi.domain.model.dto.LabReviewSearchDTO;
import umc.SukBakJi.domain.model.dto.LabReviewSummaryDTO;
import umc.SukBakJi.global.apiPayload.ApiResponse;
import umc.SukBakJi.domain.model.dto.LabReviewCreateDTO;
import umc.SukBakJi.domain.model.dto.LabReviewDetailsDTO;
import umc.SukBakJi.domain.service.LabReviewService;
import umc.SukBakJi.global.apiPayload.code.ErrorReasonDTO;
import umc.SukBakJi.global.security.jwt.JwtTokenProvider;

import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/api/labs/reviews")
public class LabReviewController {
    @Autowired
    private LabReviewService labReviewService;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

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
            @RequestHeader("Authorization") String token,
            @RequestBody LabReviewCreateDTO dto) {

        // JWT 토큰에서 Bearer 제거
        String jwt = token.substring(7);

        // JWT에서 사용자 ID 추출
        Long userId = jwtTokenProvider.getMemberIdFromToken(jwt);

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

//    @Operation(summary = "Search Lab Reviews by Professor Name", description = "지도교수명으로 연구실 후기를 검색합니다.")
//    @ApiResponses(value = {
//            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "성공",
//                    content = @Content(mediaType = "application/json",
//                            schema = @Schema(implementation = LabReviewDetailsDTO.class))),
//            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "해당 지도교수를 찾을 수 없습니다.",
//                    content = @Content(mediaType = "application/json",
//                            schema = @Schema(implementation = ErrorReasonDTO.class))),
//            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "연구실 후기를 찾을 수 없습니다.",
//                    content = @Content(mediaType = "application/json",
//                            schema = @Schema(implementation = ErrorReasonDTO.class))),
//            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "서버 에러, 관리자에게 문의 바랍니다.",
//                    content = @Content(mediaType = "application/json",
//                            schema = @Schema(implementation = ErrorReasonDTO.class)))
//    })
//    @PostMapping("/search")
//    public ApiResponse<List<LabReviewDetailsDTO>> searchLabReview(
//            @RequestBody LabReviewSearchDTO searchDTO,
//            @RequestParam(defaultValue = "0") int page,
//            @RequestParam(defaultValue = "6") int size
//    ) {
//        List<LabReviewDetailsDTO> reviewList = labReviewService.searchLabReviews(searchDTO.getProfessorName(), page, size);
//        return ApiResponse.onSuccess(reviewList);
//    }
}
