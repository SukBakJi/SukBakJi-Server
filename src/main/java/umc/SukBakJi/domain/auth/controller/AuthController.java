package umc.SukBakJi.domain.auth.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Response;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import umc.SukBakJi.domain.auth.model.dto.RefreshTokenRequest;
import umc.SukBakJi.domain.auth.model.dto.OAuth2RequestDTO;
import umc.SukBakJi.domain.member.model.dto.MemberRequestDto;
import umc.SukBakJi.domain.member.model.dto.MemberResponseDto;
import umc.SukBakJi.domain.auth.service.AuthService;
import umc.SukBakJi.global.apiPayload.ApiResponse;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;

    @PostMapping("/signup")
    @Operation(summary = "일반 회원가입", description = "이메일과 비밀번호를 입력하여 회원가입을 진행합니다.")
    public ResponseEntity<ApiResponse<String>> signUp(@RequestBody @Valid MemberRequestDto.SignUpDto requestDto) {
        authService.signUp(requestDto);
        return ResponseEntity.ok(ApiResponse.onSuccess("회원가입에 성공하였습니다."));
    }

    @PostMapping("/login")
    @Operation(summary = "일반 로그인", description = "일반 회원가입 시 입력했던 이메일과 비밀번호로 로그인합니다.")
    public ResponseEntity<ApiResponse<MemberResponseDto.LoginResponseDto>> login(@RequestBody MemberRequestDto.LoginDto requestDto) {
        MemberResponseDto.LoginResponseDto responseDto = authService.login(requestDto);
        return ResponseEntity.ok(ApiResponse.onSuccess(responseDto));
    }

    // 테스트용
    @GetMapping("/kakao-token")
    public ResponseEntity<String> getKakaoToken(@RequestParam String code) {
        String tokenUri = "https://kauth.kakao.com/oauth/token";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", "56f99bce8ffde665f895fa0169d0c751");
        params.add("client_secret", "G9KBpEbRYQzCYjt6Yc0Ic0ztvpmPGL1s");
        params.add("redirect_uri", "http://localhost:8080/login/oauth2/code/kakao");
        params.add("code", code);

        // HTTP 요청 생성
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);

        // RestTemplate을 사용하여 POST 요청 보내기
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response;
        try {
            response = restTemplate.postForEntity(tokenUri, request, String.class);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("401 Unauthorized: " + e.getMessage());
        }

        return ResponseEntity.ok(response.getBody());
    }

    // OAuth2 로그인 (카카오, 애플)
    @PostMapping("/oauth2/login")
    @Operation(summary = "OAuth2 로그인", description = "OAuth2 로그인 후 액세스 토큰을 전달해 JWT를 발급받습니다. 존재하지 않는 회원이라면 회원가입을 진행하고 이미 존재하는 회원이라면 로그인을 진행합니다.")
    public ResponseEntity<ApiResponse<MemberResponseDto.LoginResponseDto>> oauthLogin(@RequestBody OAuth2RequestDTO request) {
        MemberResponseDto.LoginResponseDto response = authService.oauthLogin(request.getProvider(), request.getAccessToken());
        return ResponseEntity.ok(ApiResponse.onSuccess(response));
    }

    @PostMapping("/email")
    @Operation(summary = "이메일 중복 확인", description = "이메일 중복 검사를 통해 이메일을 사용할 수 있는지 확인합니다.")
    public ResponseEntity<ApiResponse<?>> verifyEmail(@RequestBody @Valid MemberRequestDto.EmailDto request) {
        Boolean isAvailable = authService.verifyEmail(request.getEmail());
        if (isAvailable) {
            return new ResponseEntity<>(ApiResponse.onSuccess("사용 가능한 이메일입니다."), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(ApiResponse.onSuccess("이미 가입된 이메일입니다."), HttpStatus.CONFLICT);
        }
    }

    @PostMapping("/refresh-token")
    @Operation(summary = "토큰 재발급", description = "Refresh Token을 입력하여 토큰 재발급을 진행합니다.")
    public ResponseEntity<ApiResponse<?>> refreshToken(@RequestBody RefreshTokenRequest refreshTokenRequest) {
        log.info("Received refresh token request: " + refreshTokenRequest.getRefreshToken());
        try {
            MemberResponseDto.LoginResponseDto responseDto = authService.refreshAccessToken(refreshTokenRequest.getRefreshToken());
            return ResponseEntity.ok(ApiResponse.onSuccess(responseDto));
        } catch (Exception e) {
            log.error("Error processing refresh token request", e);
            return ResponseEntity.ok(ApiResponse.onSuccess(e.getMessage()));
        }
    }

    @PostMapping("/logout")
    @Operation(summary = "로그아웃", description = "로그인한 사용자가 로그아웃 처리됩니다.")
    public ResponseEntity<ApiResponse<?>> logout() {
        // 현재 사용자 인증 정보 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()) {
            // 현재 사용자의 인증 정보 무효화
            SecurityContextHolder.getContext().setAuthentication(null);
        }
        authService.logOut(authentication.getName());
        return ResponseEntity.ok(ApiResponse.onSuccess("로그아웃되었습니다.", authentication.getName()));
    }
}