package umc.SukBakJi.domain.auth.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import umc.SukBakJi.domain.auth.model.dto.CertificationDTO;
import umc.SukBakJi.domain.auth.service.SmsService;
import umc.SukBakJi.global.apiPayload.ApiResponse;

@Tag(name = "SMS 인증 API", description = "휴대폰 SMS 인증 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/sms/phone")
public class SmsController {
    private final SmsService smsService;

    @PostMapping()
    @Operation(summary = "SMS 인증번호 요청", description = "입력한 전화번호로 SMS 인증번호를 전송합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "인증번호 전송 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "서버 오류")
    })
    public ResponseEntity<ApiResponse<String>> sendVerificationCode(@Valid @RequestBody CertificationDTO.smsRequestDto requestDto) {
        smsService.sendVerificationCode(requestDto.getPhoneNumber());
        return ResponseEntity.ok(ApiResponse.onSuccess("인증번호가 전송되었습니다."));
    }

    @PostMapping("/verify")
    @Operation(summary = "인증번호 검증", description = "입력한 인증번호가 맞으면 회원가입을 계속 진행합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "인증 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "인증 실패 혹은 만료된 인증번호"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "서버 오류")
    })
    public ResponseEntity<ApiResponse<String>> verifyCode(@Valid @RequestBody CertificationDTO.smsVerifyDto requestDto) {
        smsService.verifyCode(requestDto);
        return ResponseEntity.ok(ApiResponse.onSuccess("인증번호 인증이 완료되었습니다."));
    }
}