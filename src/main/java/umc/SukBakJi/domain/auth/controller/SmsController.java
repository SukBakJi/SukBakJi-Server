package umc.SukBakJi.domain.auth.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import umc.SukBakJi.domain.auth.model.dto.CertificationDTO;
import umc.SukBakJi.domain.auth.service.SmsService;
import umc.SukBakJi.global.apiPayload.ApiResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/sms/phone")
public class SmsController {
    private final SmsService smsService;

    @PostMapping()
    @Operation(summary = "SMS 인증번호 요청", description = "입력한 전화번호로 SMS 인증번호를 전송합니다.")
    public ResponseEntity<ApiResponse<String>> sendVerificationCode(@Valid @RequestBody CertificationDTO.smsRequestDto requestDto) {
        smsService.sendVerificationCode(requestDto.getPhoneNumber());
        return ResponseEntity.ok(ApiResponse.onSuccess("인증번호가 전송되었습니다."));
    }

    @PostMapping("/verify")
    @Operation(summary = "인증번호 검증", description = "입력한 인증번호가 맞으면 회원가입을 계속 진행합니다.")
    public ResponseEntity<ApiResponse<String>> verifyCode(@Valid @RequestBody CertificationDTO.smsVerifyDto requestDto) {
        smsService.verifyCode(requestDto);
        return ResponseEntity.ok(ApiResponse.onSuccess("인증번호 인증이 완료되었습니다."));
    }
}