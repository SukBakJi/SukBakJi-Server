package umc.SukBakJi.domain.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
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

import java.util.List;

@RestController
@RequestMapping("/api/calender")
public class CalenderController {
    @Autowired
    private CalenderService calenderService;

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
    @GetMapping("/univ")
    public ApiResponse<UnivResponseDTO.getMethodListDTO> getMethodList(@RequestParam(name="univId") Long univId){
        List<String> scheduleInfoList = calenderService.getMethodList(univId);
        return ApiResponse.onSuccess(UnivConverter.toGetMethodListDTO(scheduleInfoList, univId));
    }

    @Operation(summary = "Set University", description = "대학교를 선택합니다.")
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
        SetUniv setUniv = calenderService.setUniv(request);
        return ApiResponse.onSuccess(UnivConverter.toSetUnivDTO(setUniv));
    }

    @Operation(summary = "Get University", description = "선택한 학교를 조회합니다.")
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
    @GetMapping("/univ/{memberId}")
    public ApiResponse<UnivResponseDTO.getUnivListDTO> getUnivList(@PathVariable(name="memberId") Long memberId){
        List<SetUniv> univList = calenderService.getUnivList(memberId);
        if(univList == null){
            return ApiResponse.onSuccess(UnivConverter.toGetUnivListDTO(memberId, null));
        }
        return ApiResponse.onSuccess(UnivConverter.toGetUnivListDTO(memberId, univList));
    }

    @Operation(summary = "Set Alarm", description = "알람을 설정합니다.")
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

    @Operation(summary = "Get Alarm", description = "알람을 조회합니다.")
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
    @GetMapping("/alarm/{memberId}")
    public ApiResponse<AlarmResponseDTO.getAlarmListDTO> viewAlarmList(@PathVariable(name="memberId") Long memberId){
        List<Alarm> alarmList = calenderService.getAlarmList(memberId);
        if(alarmList == null){
            return ApiResponse.onSuccess(AlarmConverter.getAlarmListDTO(memberId, null));
        }
        return ApiResponse.onSuccess(AlarmConverter.getAlarmListDTO(memberId, alarmList));
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
