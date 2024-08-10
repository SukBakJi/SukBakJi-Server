package umc.SukBakJi.domain.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import umc.SukBakJi.domain.converter.MentoringConverter;
import umc.SukBakJi.domain.model.dto.MentoringRequestDTO;
import umc.SukBakJi.domain.model.dto.MentoringResponseDTO;
import umc.SukBakJi.domain.service.MentoringService;
import umc.SukBakJi.global.apiPayload.ApiResponse;
import umc.SukBakJi.global.apiPayload.code.ErrorReasonDTO;

@RestController
@RequestMapping("/api/mentor")
public class MentoringController {

    @Autowired
    private MentoringService mentoringService;

    @Operation(summary = "멘토 신청", description = "멘토로 신청합니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "성공",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = MentoringResponseDTO.setMentorDTO.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "사용자가 유효하지 않습니다.",
                    content = @Content),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "서버 에러, 관리자에게 문의 바랍니다.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorReasonDTO.class)))
    })
    @PostMapping("")
    public ApiResponse<MentoringResponseDTO.setMentorDTO> setMentor(
            @Parameter(description = "멘토 신청 DTO", required = true)
            @RequestBody MentoringRequestDTO.applyMentor request) {
        mentoringService.setMentor(request);
        return ApiResponse.onSuccess(MentoringConverter.toSetMentorDTO(request.getMemberId()));
    }

//    @Operation(summary = "멘토 목록", description = "멘토 목록을 조회합니다.")
//    @ApiResponses(value = {
//            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "성공",
//                    content = @Content(mediaType = "application/json",
//                            schema = @Schema(implementation = MentoringResponseDTO.setMentorDTO.class))),
//            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "사용자가 유효하지 않습니다.",
//                    content = @Content),
//            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "서버 에러, 관리자에게 문의 바랍니다.",
//                    content = @Content(mediaType = "application/json",
//                            schema = @Schema(implementation = ErrorReasonDTO.class)))
//    })
//    @GetMapping("")
//    public ApiResponse<MentoringResponseDTO.getMentorDTO> getMentor() {
//        mentoringService.setMentor(request.getMemberId());
//        return ApiResponse.onSuccess(MentoringConverter.toSetMentorDTO(request.getMemberId()));
//    }
    @Operation(summary = "멘토링 신청", description = "멘토링을 신청합니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "성공",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = MentoringResponseDTO.setMentoringDTO.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "사용자가 유효하지 않습니다.",
                    content = @Content),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "서버 에러, 관리자에게 문의 바랍니다.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorReasonDTO.class)))
    })
    @PostMapping("/mentoring")
    public ApiResponse<MentoringResponseDTO.setMentoringDTO> setMentor(
            @Parameter(description = "멘토링 신청 DTO", required = true)
            @RequestBody MentoringRequestDTO.applyMentoring request) {
        mentoringService.setMentoring(request);
        return ApiResponse.onSuccess(MentoringConverter.toSetMentoringDTO(request.getMentorId(), request.getMemberId()));
    }
}
