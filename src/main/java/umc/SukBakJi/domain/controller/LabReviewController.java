package umc.SukBakJi.domain.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import umc.SukBakJi.global.apiPayload.ApiResponse;
import umc.SukBakJi.domain.model.dto.LabReviewCreateDTO;
import umc.SukBakJi.domain.model.dto.LabReviewDetailsDTO;
import umc.SukBakJi.domain.service.LabReviewService;

@RestController
@RequestMapping("/api/labs/reviews")
public class LabReviewController {
    @Autowired
    private LabReviewService labReviewService;

    @Operation(summary = "Get Lab Review Details", description = "연구실 후기 상세 정보를 조회합니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "성공",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = LabReviewDetailsDTO.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "연구실 후기를 찾을 수 없습니다.",
                    content = @Content)
    })
    @GetMapping("/{reviewId}")
    public ApiResponse<LabReviewDetailsDTO> getLabReviewDetails(@PathVariable Long reviewId) {
        LabReviewDetailsDTO details = labReviewService.getLabReviewDetails(reviewId);
        return ApiResponse.onSuccess(details);
    }

    @Operation(summary = "Create Lab Review", description = "연구실 후기를 작성합니다.")
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
                    content = @Content)
    })
    @PostMapping
    public ApiResponse<LabReviewDetailsDTO> createLabReview(
            @Parameter(description = "연구실 후기 생성 DTO", required = true)
            @RequestBody LabReviewCreateDTO dto) {
        LabReviewDetailsDTO details = labReviewService.createLabReview(dto);
        return ApiResponse.onSuccess(details);
    }
}
