package umc.SukBakJi.domain.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import umc.SukBakJi.domain.model.dto.CertificationDTO;
import umc.SukBakJi.domain.service.SmsService;
import umc.SukBakJi.global.apiPayload.ApiResponse;
import umc.SukBakJi.global.apiPayload.code.status.ErrorStatus;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/sms")
public class SmsController {
    private final SmsService smsService;

    @PostMapping("/code")
    @Operation(summary = "SMS 인증번호 요청", description = "입력한 전화번호로 SMS 인증번호를 전송합니다.")
    public ResponseEntity<ApiResponse<String>> sendVerificationCode(@RequestBody CertificationDTO.smsRequestDto requestDto) {
        smsService.sendVerificationCode(requestDto.getPhoneNumber());
        return ResponseEntity.ok(ApiResponse.onSuccess("인증번호가 전송되었습니다."));
    }

    @PostMapping("/find-email")
    @Operation(summary = "인증번호 검증 후 이메일 찾기", description = "입력한 인증번호가 맞으면 등록된 이메일을 반환합니다.")
    public ResponseEntity<ApiResponse<String>> verifyCode(@RequestBody CertificationDTO.smsVerifyDto requestDto) {
        String result = smsService.verifyAndFindEmail(requestDto);

        switch (result) {
            case "INVALID_CODE":
                return ResponseEntity.badRequest().body(ApiResponse.onFailure(
                        ErrorStatus.INVALID_CODE.getCode(), ErrorStatus.INVALID_CODE.getMessage(), null
                ));
            case "EMAIL_NOT_FOUND":
                return ResponseEntity.ok(ApiResponse.onFailure(
                        ErrorStatus.EMAIL_NOT_FOUND.getCode(), ErrorStatus.EMAIL_NOT_FOUND.getMessage(), null
                ));
            default:
                return ResponseEntity.ok(ApiResponse.onSuccess(result));
        }
    }
}
