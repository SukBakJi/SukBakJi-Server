package umc.SukBakJi.domain.auth.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import umc.SukBakJi.domain.auth.model.dto.AuthRequestDTO;
import umc.SukBakJi.domain.auth.model.dto.RefreshTokenRequest;
import umc.SukBakJi.domain.auth.model.dto.OAuth2RequestDTO;
import umc.SukBakJi.domain.member.model.dto.MemberRequestDTO;
import umc.SukBakJi.domain.member.model.dto.MemberResponseDTO;
import umc.SukBakJi.domain.auth.service.AuthService;
import umc.SukBakJi.global.apiPayload.ApiResponse;
import umc.SukBakJi.global.security.jwt.JwtBlacklistService;

@Tag(name = "인증/로그인 API", description = "회원가입, 로그인, 이메일 인증, 비밀번호 재설정 등 인증 관련 기능 제공")
@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;

    @PostMapping("/signup")
    @Operation(summary = "일반 회원가입", description = "전화번호, 이메일, 비밀번호를 입력하여 회원가입을 진행합니다.")
    public ResponseEntity<ApiResponse<String>> signUp(@RequestBody @Valid AuthRequestDTO.SignUpDto requestDto) {
        authService.signUp(requestDto);
        return ResponseEntity.ok(ApiResponse.onSuccess("회원가입에 성공하였습니다."));
    }

    @PostMapping("/login")
    @Operation(summary = "일반 로그인", description = "일반 회원가입 시 입력했던 이메일과 비밀번호로 로그인합니다.")
    public ResponseEntity<ApiResponse<MemberResponseDTO.LoginResponseDto>> login(@RequestBody AuthRequestDTO.LoginDto requestDto) {
        MemberResponseDTO.LoginResponseDto responseDto = authService.login(requestDto);
        return ResponseEntity.ok(ApiResponse.onSuccess(responseDto));
    }

    // OAuth2 로그인
    @PostMapping("/oauth2/login")
    @Operation(summary = "OAuth2 로그인", description = "OAuth2 로그인 후 액세스 토큰을 전달해 JWT를 발급받습니다. 존재하지 않는 회원이라면 회원가입을 진행하고 이미 존재하는 회원이라면 로그인을 진행합니다.")
    public ResponseEntity<ApiResponse<MemberResponseDTO.LoginResponseDto>> oauthLogin(@RequestBody OAuth2RequestDTO request) {
        MemberResponseDTO.LoginResponseDto response = authService.oauthLogin(request.getProvider(), request.getAccessToken());
        return ResponseEntity.ok(ApiResponse.onSuccess(response));
    }

    @PostMapping("/email")
    @Operation(summary = "이메일 중복 확인", description = "이메일 중복 검사를 통해 이메일을 사용할 수 있는지 확인합니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "사용 가능한 이메일"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "409", description = "이미 존재하는 이메일"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "서버 오류")
    })
    public ResponseEntity<ApiResponse<?>> verifyEmail(@RequestBody @Valid AuthRequestDTO.EmailDto request) {
        Boolean isAvailable = authService.verifyEmail(request.getEmail());
        if (isAvailable) {
            return new ResponseEntity<>(ApiResponse.onSuccess("사용 가능한 이메일입니다."), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(ApiResponse.onSuccess("이미 가입된 이메일입니다."), HttpStatus.CONFLICT);
        }
    }

    @PostMapping("/member-email")
    @Operation(summary = "이름과 전화번호로 이메일 찾기", description = "이름과 전화번호로 등록된 이메일을 일부 반환합니다.")
    public ResponseEntity<ApiResponse<String>> findEmail(@RequestBody MemberRequestDTO.SearchEmailDto requestDto) {
        String response = authService.findEmail(requestDto);
        return ResponseEntity.ok(ApiResponse.onSuccess(response));
    }

    @PostMapping("/password")
    @Operation(summary = "비밀번호 찾기", description = "이메일을 입력하여 해당 이메일로 인증번호를 전송합니다.")
    public ResponseEntity<ApiResponse<String>> findPassword(@Valid @RequestBody MemberRequestDTO.SearchPasswordDto searchPasswordDto) throws MessagingException {
        authService.searchPassword(searchPasswordDto);
        return ResponseEntity.ok(ApiResponse.onSuccess("비밀번호 재설정에 필요한 인증번호가 이메일로 전송되었습니다."));
    }

    @PostMapping("/email-code")
    @Operation(summary = "이메일 인증번호 인증", description = "이메일로 전달된 인증번호를 검사합니다.")
    public ResponseEntity<ApiResponse<String>> verifyEmailCode(@Valid @RequestBody MemberRequestDTO.EmailCodeDto emailCodeDto) throws MessagingException {
        String response = authService.verifyEmailCode(emailCodeDto);
        return ResponseEntity.ok(ApiResponse.onSuccess(response));
    }

    @PostMapping("/refresh-token")
    @Operation(summary = "토큰 재발급", description = "Refresh Token을 입력하여 토큰 재발급을 진행합니다.")
    public ResponseEntity<ApiResponse<?>> refreshToken(@RequestBody RefreshTokenRequest refreshTokenRequest) {
        log.info("Received refresh token request: " + refreshTokenRequest.getRefreshToken());
        try {
            MemberResponseDTO.LoginResponseDto responseDto = authService.refreshAccessToken(refreshTokenRequest.getRefreshToken());
            return ResponseEntity.ok(ApiResponse.onSuccess(responseDto));
        } catch (Exception e) {
            log.error("Error processing refresh token request", e);
            return ResponseEntity.ok(ApiResponse.onSuccess(e.getMessage()));
        }
    }

    @PostMapping("/password-reset")
    @Operation(summary = "이메일 인증에서 비밀번호 재설정", description = "이메일 인증 후, 비밀번호를 재설정합니다.")
    public ApiResponse<String> resetPassword(@Valid @RequestBody AuthRequestDTO.LoginDto modifyPasswordDto) {
        authService.resetPassword(modifyPasswordDto);
        return ApiResponse.onSuccess("비밀번호를 재설정하였습니다.");
    }

    @PostMapping("/logout")
    @Operation(summary = "로그아웃", description = "로그인한 사용자가 로그아웃 처리됩니다.")
    public ResponseEntity<ApiResponse<String>> logout(@RequestHeader("Authorization") String tokenHeader) {
        String jwtToken = tokenHeader.substring(7);
        authService.logOut(jwtToken);
        return ResponseEntity.ok(ApiResponse.onSuccess("로그아웃되었습니다."));
    }
}