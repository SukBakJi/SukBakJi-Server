package umc.SukBakJi.domain.alarm.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import umc.SukBakJi.domain.alarm.converter.AlarmConverter;
import umc.SukBakJi.domain.alarm.service.AlarmService;
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

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/calender")
public class AlarmController {
    private final AlarmService alarmService;

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
        Alarm alarm = alarmService.createAlarm(request);
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
    public ApiResponse<AlarmResponseDTO.getAlarmListDTO> viewAlarmList(@AuthenticationPrincipal PrincipalDetails principalDetails) {
        Long memberId = principalDetails.getMember().getMemberId();

        List<Alarm> alarmList = alarmService.getAlarmList(memberId);
        if (alarmList == null) {
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
    public ApiResponse<AlarmResponseDTO.alarmDTO> updateAlarm(@PathVariable Long alarmId, @RequestBody @Valid AlarmRequestDTO.updateAlarm request){
        AlarmResponseDTO.alarmDTO updatedAlarm = alarmService.updateAlarm(alarmId, request);
        return ApiResponse.onSuccess("알람이 성공적으로 수정되었습니다.", updatedAlarm);
    }

    @Operation(summary = "알람 설정 화면에서 대학교 목록 조회", description = "알람 설정 시 대학 선택에서 대학교 목록을 조회합니다")
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
    @GetMapping("/alarm/univ")
    public ApiResponse<List<String>> getAlarmUnivList() {
        List<String> univList = alarmService.getAlarmUnivList();
        return ApiResponse.onSuccess(univList);
    }

    @Operation(summary = "알람 삭제", description = "알람을 삭제합니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "성공",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Object.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "올바른 날짜나 시간이 아닙니다.",
                    content = @Content),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "서버 에러, 관리자에게 문의 바랍니다.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorReasonDTO.class)))
    })
    @DeleteMapping("/alarm/{alarmId}")
    public ApiResponse<Void> deleteAlarm(@AuthenticationPrincipal PrincipalDetails principalDetails, @PathVariable Long alarmId) {
        Long memberId = principalDetails.getMember().getMemberId();

        alarmService.deleteAlarm(memberId, alarmId);
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
        Alarm alarm = alarmService.onAlarm(request.getAlarmId());
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
        Alarm alarm = alarmService.offAlarm(request.getAlarmId());
        return ApiResponse.onSuccess(AlarmConverter.turnOnOff(alarm));
    }
}