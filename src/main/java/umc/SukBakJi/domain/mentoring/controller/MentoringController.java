package umc.SukBakJi.domain.mentoring.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import umc.SukBakJi.domain.mentoring.converter.MentoringConverter;
import umc.SukBakJi.domain.mentoring.model.dto.MentoringRequestDTO;
import umc.SukBakJi.domain.mentoring.model.dto.MentoringResponseDTO;
import umc.SukBakJi.domain.mentoring.service.MentoringService;
import umc.SukBakJi.global.apiPayload.ApiResponse;
import umc.SukBakJi.global.apiPayload.code.ErrorReasonDTO;
import umc.SukBakJi.global.security.jwt.JwtTokenProvider;

import java.util.List;
@Tag(name = "멘토링", description = "멘토 등록, 멘토 조회, 멘토링 신청 등 멘토링 관련 API")
@RestController
@RequestMapping("/api/mentor")
public class MentoringController {

    @Autowired
    private MentoringService mentoringService;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

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

    @Operation(summary = "멘토 목록", description = "멘토 목록을 조회합니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "성공",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = MentoringResponseDTO.getMentorDTO.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "사용자가 유효하지 않습니다.",
                    content = @Content),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "서버 에러, 관리자에게 문의 바랍니다.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorReasonDTO.class)))
    })
    @GetMapping("")
    public ApiResponse<MentoringResponseDTO.getMentorDTO> getMentor(@RequestHeader("Authorization") String token){
        String jwtToken = token.substring(7);
        Long memberId = jwtTokenProvider.getMemberIdFromToken(jwtToken);
        List<MentoringResponseDTO.MentorDTO> mentorList = mentoringService.getMentor(memberId);
        return ApiResponse.onSuccess(MentoringConverter.toGetMentoringDTO(memberId, mentorList));
    }

    @Operation(summary = "멘토 검색", description = "멘토를 검색합니다.")
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
    @GetMapping("/search")
    public ApiResponse<MentoringResponseDTO.getMentorDTO> searchMentor(@RequestHeader("Authorization") String token, @RequestParam("keyword") String keyword){
        String jwtToken = token.substring(7);
        Long memberId = jwtTokenProvider.getMemberIdFromToken(jwtToken);
        List<MentoringResponseDTO.MentorDTO> mentorList = mentoringService.searchMentor(keyword);
        return ApiResponse.onSuccess(MentoringConverter.toGetMentoringDTO(memberId, mentorList));
    }

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
