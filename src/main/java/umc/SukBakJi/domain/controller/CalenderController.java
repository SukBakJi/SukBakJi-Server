package umc.SukBakJi.domain.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import umc.SukBakJi.domain.converter.AlarmConverter;
import umc.SukBakJi.domain.converter.UnivConverter;
import umc.SukBakJi.domain.model.dto.AlarmRequestDTO;
import umc.SukBakJi.domain.model.dto.AlarmResponseDTO;
import umc.SukBakJi.domain.model.dto.UnivRequestDTO;
import umc.SukBakJi.domain.model.dto.UnivResponseDTO;
import umc.SukBakJi.domain.model.entity.Alarm;
import umc.SukBakJi.domain.model.entity.UnivScheduleInfo;
import umc.SukBakJi.domain.model.entity.University;
import umc.SukBakJi.domain.model.entity.mapping.SetUniv;
import umc.SukBakJi.domain.service.CalenderService;
import umc.SukBakJi.global.apiPayload.ApiResponse;
import umc.SukBakJi.global.apiPayload.code.ErrorReasonDTO;
import umc.SukBakJi.global.security.jwt.JwtTokenProvider;

import java.util.List;

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
    public ApiResponse<UnivResponseDTO.memberIdDTO> getMemberId(@AuthenticationPrincipal Long memberId){
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
    public ApiResponse<UnivResponseDTO.getUnivListDTO> getUnivList(@AuthenticationPrincipal Long memberId){
        List<SetUniv> univList = calenderService.getUnivList(memberId);
        if(univList == null){
            return ApiResponse.onSuccess(UnivConverter.toGetUnivListDTO(memberId, null));
        }
        return ApiResponse.onSuccess(UnivConverter.toGetUnivListDTO(memberId, univList));
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
    public ApiResponse<UnivResponseDTO.getScheduleListDTO> getSchedule(@AuthenticationPrincipal Long memberId) {
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
    public ApiResponse<UnivResponseDTO.getSpeciDateListDTO> getSpeciDateSchedule(@AuthenticationPrincipal Long memberId, @RequestParam("date") String date){
        List<UnivScheduleInfo> univScheduleInfoList = calenderService.getSpeciDateScheduleList(memberId, date);
        List<UnivResponseDTO.speciDateListDTO> speciDateList = UnivConverter.speciDateList(univScheduleInfoList);
        return ApiResponse.onSuccess(UnivConverter.toGetSpeciDateList(memberId, speciDateList));
    }

    @Operation(summary = "알람 설정", description = "알람을 설정합니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "성공",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AlarmResponseDTO.createAlarmDTO.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "올바른 날짜나 시간이 아닙니다.",
                    content = @Content),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "서버 에러, 관리자에게 문의 바랍니다.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorReasonDTO.class)))
    })
    @PostMapping("/alarm")
    public ApiResponse<AlarmResponseDTO.createAlarmDTO> createAlarm(@RequestBody @Valid AlarmRequestDTO.createAlarm request){
        Alarm alarm = calenderService.createAlarm(request);
        return ApiResponse.onSuccess(AlarmConverter.toCreateAlarm(alarm));
    }

    @Operation(summary = "알람 조회", description = "알람을 조회합니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "성공",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AlarmResponseDTO.getAlarmListDTO.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "해당하는 사용자가 없습니다.",
                    content = @Content),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "서버 에러, 관리자에게 문의 바랍니다.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorReasonDTO.class)))
    })
    @GetMapping("/alarm")
    public ApiResponse<AlarmResponseDTO.getAlarmListDTO> viewAlarmList(@AuthenticationPrincipal Long memberId){
        List<Alarm> alarmList = calenderService.getAlarmList(memberId);
        if(alarmList == null){
            return ApiResponse.onSuccess(AlarmConverter.getAlarmListDTO(memberId, null));
        }
        return ApiResponse.onSuccess(AlarmConverter.getAlarmListDTO(memberId, alarmList));
    }

    @Operation(summary = "알람 수정", description = "알람을 수정합니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "성공",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AlarmResponseDTO.alarmDTO.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "올바른 날짜나 시간이 아닙니다.",
                    content = @Content),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "알람을 수정할 권한이 없습니다.",
                    content = @Content),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "존재하지 않는 알람입니다.",
                    content = @Content),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "서버 에러, 관리자에게 문의 바랍니다.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorReasonDTO.class)))
    })
    @PatchMapping("/alarm/{alarmId}")
    public ApiResponse<AlarmResponseDTO.alarmDTO> updateAlarm(@AuthenticationPrincipal Long alarmId, @RequestBody @Valid AlarmRequestDTO.updateAlarm request){
        AlarmResponseDTO.alarmDTO updatedAlarm = calenderService.updateAlarm(alarmId, request);
        return ApiResponse.onSuccess("알람이 성공적으로 수정되었습니다.", updatedAlarm);
    }

    @Operation(summary = "알람 삭제", description = "알람을 삭제합니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "성공",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = String.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "올바른 날짜나 시간이 아닙니다.",
                    content = @Content),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "서버 에러, 관리자에게 문의 바랍니다.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorReasonDTO.class)))
    })
    @DeleteMapping("/alarm/{alarmId}")
    public ApiResponse<Void> deleteAlarm(@AuthenticationPrincipal Long memberId, @PathVariable Long alarmId) {
        calenderService.deleteAlarm(memberId, alarmId);
        return ApiResponse.onSuccess("알람이 성공적으로 삭제되었습니다.", null);
    }

    @Operation(summary = "알람 켜기", description = "알람을 킵니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "성공",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AlarmResponseDTO.turnOnOff.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "올바른 날짜나 시간이 아닙니다.",
                    content = @Content),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "서버 에러, 관리자에게 문의 바랍니다.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorReasonDTO.class)))
    })
    @PatchMapping("/alarm/on")
    public ApiResponse<AlarmResponseDTO.turnOnOff> turnOnAlarm(@RequestBody @Valid AlarmRequestDTO.turnAlarm request){
        Alarm alarm = calenderService.onAlarm(request.getAlarmId());
        return ApiResponse.onSuccess(AlarmConverter.turnOnOff(alarm));
    }

    @Operation(summary = "알람 끄기", description = "알람을 끕니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "성공",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AlarmResponseDTO.turnOnOff.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "올바른 날짜나 시간이 아닙니다.",
                    content = @Content),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "서버 에러, 관리자에게 문의 바랍니다.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorReasonDTO.class)))
    })
    @PatchMapping("/alarm/off")
    public ApiResponse<AlarmResponseDTO.turnOnOff> turnOffAlarm(@RequestBody @Valid AlarmRequestDTO.turnAlarm request){
        Alarm alarm = calenderService.offAlarm(request.getAlarmId());
        return ApiResponse.onSuccess(AlarmConverter.turnOnOff(alarm));
    }
}
