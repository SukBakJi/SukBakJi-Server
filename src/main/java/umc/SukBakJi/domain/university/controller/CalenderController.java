package umc.SukBakJi.domain.university.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import umc.SukBakJi.domain.alarm.converter.AlarmConverter;
import umc.SukBakJi.domain.university.converter.UnivConverter;
import umc.SukBakJi.domain.alarm.model.dto.AlarmRequestDTO;
import umc.SukBakJi.domain.alarm.model.dto.AlarmResponseDTO;
import umc.SukBakJi.domain.university.model.dto.UnivRequestDTO;
import umc.SukBakJi.domain.university.model.dto.UnivResponseDTO;
import umc.SukBakJi.domain.alarm.model.entity.Alarm;
import umc.SukBakJi.domain.university.model.entity.UnivScheduleInfo;
import umc.SukBakJi.domain.common.entity.mapping.SetUniv;
import umc.SukBakJi.domain.university.service.CalenderService;
import umc.SukBakJi.global.apiPayload.ApiResponse;
import umc.SukBakJi.global.apiPayload.code.ErrorReasonDTO;
import umc.SukBakJi.global.security.PrincipalDetails;
import umc.SukBakJi.global.security.jwt.JwtTokenProvider;

import java.util.List;

@Tag(name = "학사 일정", description = "대학교 검색, 일정 조회, 등록, 수정, 삭제 등의 학사 일정 관련 API")
@RestController
@RequestMapping("/api/calender")
public class CalenderController {
    @Autowired
    private CalenderService calenderService;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Operation(summary = "사용자 id 조회", description = "로그인된 사용자의 id를 조회합니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "성공",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UnivResponseDTO.getSearchListDTO.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "해당하는 사용자가 없습니다.",
                    content = @Content),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "서버 에러, 관리자에게 문의 바랍니다.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorReasonDTO.class)))
    })
    @GetMapping("/member")
    public ApiResponse<UnivResponseDTO.memberIdDTO> getMemberId(@AuthenticationPrincipal PrincipalDetails principalDetails){
        Long memberId = principalDetails.getMember().getMemberId();
        UnivResponseDTO.memberIdDTO memberIdDTO = new UnivResponseDTO.memberIdDTO(memberId);
        return ApiResponse.onSuccess(memberIdDTO);
    }
  
  @Operation(summary = "대학교 이름 조회", description = "대학교에 해당하는 이름을 조회합니다")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "성공",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UnivResponseDTO.getUnivIdDTO.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "해당하는 사용자가 없습니다.",
                    content = @Content),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "서버 에러, 관리자에게 문의 바랍니다.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorReasonDTO.class)))
    })
    @GetMapping("")
    public ApiResponse<UnivResponseDTO.getUnivIdDTO> getUnivId(@RequestParam(name="univId") Long univId){
        UnivResponseDTO.getUnivIdDTO univInfo = calenderService.getUnivId(univId);
        return ApiResponse.onSuccess(univInfo);
    }

    @Operation(summary = "대학교 검색", description = "학교를 검색합니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "성공",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UnivResponseDTO.getSearchListDTO.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "해당하는 사용자가 없습니다.",
                    content = @Content),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "서버 에러, 관리자에게 문의 바랍니다.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorReasonDTO.class)))
    })
    @GetMapping("/search")
    public ApiResponse<UnivResponseDTO.getSearchListDTO> getMethodList(@RequestParam(name="keyword") String keyword){
        List<UnivResponseDTO.searchListDTO> searchList = calenderService.getSearchList(keyword);
        return ApiResponse.onSuccess(UnivConverter.toGetSearchListDTO(searchList));
    }

    @Operation(summary = "대학교 모집 전형 조회", description = "선택한 학교의 모집 전형을 조회합니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "성공",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UnivResponseDTO.getMethodListDTO.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "해당하는 사용자가 없습니다.",
                    content = @Content),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "서버 에러, 관리자에게 문의 바랍니다.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorReasonDTO.class)))
    })
    @GetMapping("/univ/method")
    public ApiResponse<UnivResponseDTO.getMethodListDTO> getMethodList(@RequestParam(name="univId") Long univId){
        List<String> scheduleInfoList = calenderService.getMethodList(univId);
        return ApiResponse.onSuccess(UnivConverter.toGetMethodListDTO(scheduleInfoList, univId));
    }

    @Operation(summary = "대학교 선택", description = "대학교를 선택합니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "성공",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UnivResponseDTO.setUnivDTO.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "선택된 학교가 유효하지 않습니다.",
                    content = @Content),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "서버 에러, 관리자에게 문의 바랍니다.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorReasonDTO.class)))
    })
    @PostMapping("/univ")
    public ApiResponse<UnivResponseDTO.setUnivDTO> setUnivDTOApiResponse(
            @Parameter(description = "대학교 선택 DTO", required = true)
            @RequestBody UnivRequestDTO.setUniv request) {
        calenderService.setUniv(request);
        return ApiResponse.onSuccess(UnivConverter.toSetUnivDTO(request.getMemberId()));
    }

    // TODO : 학교 조회
    @Operation(summary = "학교 조회", description = "등록한 학교를 조회합니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "성공",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UnivResponseDTO.getUnivListDTO.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "해당하는 사용자가 없습니다.",
                    content = @Content),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "서버 에러, 관리자에게 문의 바랍니다.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorReasonDTO.class)))
    })
    @GetMapping("/univ")
    public ApiResponse<UnivResponseDTO.getUnivListDTO> getUnivList(@AuthenticationPrincipal PrincipalDetails principalDetails) {
        Long memberId = principalDetails.getMember().getMemberId();
        List<SetUniv> univList = calenderService.getUnivList(memberId);
        if (univList == null) {
            return ApiResponse.onSuccess(UnivConverter.toGetUnivListDTO(memberId, null));
        }
        return ApiResponse.onSuccess(UnivConverter.toGetUnivListDTO(memberId, univList));
    }

    @Operation(summary = "학교 일정 수정", description = "학교 일정을 수정합니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "성공",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Object.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "해당하는 사용자가 없습니다.",
                    content = @Content),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "서버 에러, 관리자에게 문의 바랍니다.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorReasonDTO.class)))
    })
    @PatchMapping("/univ/{univId}")
    public ApiResponse<Void> updateUniv(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @PathVariable Long univId,
            @RequestBody UnivRequestDTO.UpdateUnivDTO request){
        Long memberId = principalDetails.getMember().getMemberId();
        calenderService.updateUnivSchedule(memberId, univId, request);
        return ApiResponse.onSuccess("대학 일정을 성공적으로 수정하였습니다.", null);
    }

    @Operation(summary = "등록된 학교 삭제", description = "등록된 학교를 삭제합니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "성공",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UnivResponseDTO.setUnivDTO.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "선택된 학교가 유효하지 않습니다.",
                    content = @Content),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "서버 에러, 관리자에게 문의 바랍니다.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorReasonDTO.class)))
    })
    @DeleteMapping("/univ")
    public ApiResponse<UnivResponseDTO.setUnivDTO> deleteUniv(
            @Parameter(description = "삭제할 학교 DTO", required = true)
            @RequestBody UnivRequestDTO.setUniv request) {
        calenderService.deleteUniv(request);
        return ApiResponse.onSuccess(UnivConverter.toSetUnivDTO(request.getMemberId()));
    }

    @Operation(summary = "등록된 학교 선택 삭제", description = "등록된 학교를 선택해서 삭제합니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "성공",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Object.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "선택된 학교가 유효하지 않습니다.",
                    content = @Content),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "서버 에러, 관리자에게 문의 바랍니다.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorReasonDTO.class)))
    })
    @DeleteMapping("/univ/selected")
    public ApiResponse<Void> deleteOptionUniv(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @Parameter(description = "삭제할 학교 DTO", required = true)
            @RequestBody UnivRequestDTO.DeleteSelectedUnivDTO request) {
        Long memberId = principalDetails.getMember().getMemberId();

        calenderService.deleteSelectedUniv(memberId, request);
        return ApiResponse.onSuccess("대학 일정을 선택 삭제하였습니다.", null);
    }

    @Operation(summary = "등록된 학교 전체 삭제", description = "등록된 학교를 전체 삭제합니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "성공",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Object.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "선택된 학교가 유효하지 않습니다.",
                    content = @Content),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "서버 에러, 관리자에게 문의 바랍니다.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorReasonDTO.class)))
    })
    @DeleteMapping("/univ/all")
    public ApiResponse<Void> deleteAllUniv(
            @AuthenticationPrincipal PrincipalDetails principalDetails) {
        Long memberId = principalDetails.getMember().getMemberId();
        calenderService.deleteAllUniv(memberId);
        return ApiResponse.onSuccess("대학 일정을 전체 삭제하였습니다.", null);
    }

    @Operation(summary = "조회할 일정 선택", description = "화면에 띄우고 싶은 일정을 선택합니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "성공",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UnivResponseDTO.setUnivDTO.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "선택된 학교가 유효하지 않습니다.",
                    content = @Content),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "서버 에러, 관리자에게 문의 바랍니다.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorReasonDTO.class)))
    })
    @PostMapping("/schedule")
    public ApiResponse<UnivResponseDTO.setUnivDTO> setSchedule(
            @Parameter(description = "일정 선택 DTO", required = true)
            @RequestBody UnivRequestDTO.setScheduleList request) {
        calenderService.setSchedule(request);
        return ApiResponse.onSuccess(UnivConverter.toSetUnivDTO(request.getMemberId()));
    }

    @Operation(summary = "다가오는 일정 목록 조회", description = "다가오는 일정 목록을 조회합니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "성공",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UnivResponseDTO.getScheduleListDTO.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "선택된 학교가 유효하지 않습니다.",
                    content = @Content),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "서버 에러, 관리자에게 문의 바랍니다.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorReasonDTO.class)))
    })
    @GetMapping("/schedule")
    public ApiResponse<UnivResponseDTO.getScheduleListDTO> getSchedule(@AuthenticationPrincipal PrincipalDetails principalDetails) {
        Long memberId = principalDetails.getMember().getMemberId();
        List<UnivScheduleInfo> univScheduleInfoList = calenderService.getScheduleList(memberId);
        List<UnivResponseDTO.scheduleListDTO> scheduleListDTOList = UnivConverter.toScheduleList(univScheduleInfoList);
        return ApiResponse.onSuccess(UnivConverter.toGetScheduleList(memberId, scheduleListDTOList));
    }

    @Operation(summary = "날짜 선택 시 일정 조회", description = "날짜를 선택하면 해당 날짜에 맞는 일정을 조회할 수 있습니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "성공",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UnivResponseDTO.setUnivDTO.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "선택된 학교가 유효하지 않습니다.",
                    content = @Content),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "서버 에러, 관리자에게 문의 바랍니다.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorReasonDTO.class)))
    })
    @GetMapping("/schedule/{date}")
    public ApiResponse<UnivResponseDTO.getSpeciDateListDTO> getSpeciDateSchedule(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @RequestParam("date") String date){
        Long memberId = principalDetails.getMember().getMemberId();
        List<UnivScheduleInfo> univScheduleInfoList = calenderService.getSpeciDateScheduleList(memberId, date);
        List<UnivResponseDTO.speciDateListDTO> speciDateList = UnivConverter.speciDateList(univScheduleInfoList);
        return ApiResponse.onSuccess(UnivConverter.toGetSpeciDateList(memberId, speciDateList));
    }
}
