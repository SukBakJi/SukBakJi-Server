package umc.SukBakJi.domain.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import umc.SukBakJi.domain.converter.AuthConverter;
import umc.SukBakJi.global.security.jwt.JwtToken;
import umc.SukBakJi.domain.model.dto.auth.kakao.KakaoDto;
import umc.SukBakJi.domain.model.dto.member.MemberRequestDto;
import umc.SukBakJi.domain.model.dto.member.MemberResponseDto;
import umc.SukBakJi.domain.model.entity.Member;
import umc.SukBakJi.domain.model.entity.enums.Provider;
import umc.SukBakJi.domain.service.AuthService;
import umc.SukBakJi.global.apiPayload.ApiResponse;
import umc.SukBakJi.global.security.jwt.JwtTokenProvider;


@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping("/signup")
    @Operation(summary = "일반 회원가입", description = "이메일과 비밀번호를 입력하여 회원가입을 진행합니다.")
    public ApiResponse<MemberResponseDto.SignUpResponseDto> signUp(@RequestBody @Valid MemberRequestDto.SignUpDto requestDto) {
        MemberResponseDto.SignUpResponseDto responseDto = authService.signUp(requestDto);
        return ApiResponse.onSuccess("회원가입에 성공하였습니다.", responseDto);
    }

    @PostMapping("/login")
    @Operation(summary = "일반 로그인", description = "일반 회원가입 시 입력했던 이메일과 비밀번호로 로그인합니다.")
    public ApiResponse<MemberResponseDto.LoginResponseDto> login(@RequestBody MemberRequestDto.LoginDto requestDto) {
        MemberResponseDto.LoginResponseDto responseDto = authService.login(requestDto);
        return ApiResponse.onSuccess(responseDto);
    }

    @PostMapping("/kakao")
    @Operation(summary = "카카오톡 회원가입 및 로그인",
               description = "카카오톡 사용자 정보로 회원가입을 진행하고 이미 존재하는 회원이라면 카카오톡으로 로그인합니다.")
    public ApiResponse<MemberResponseDto.LoginResponseDto> kakaoLogin(@RequestBody KakaoDto.KakaoRequestDto request) {

        String email = request.getEmail();

        // 액세스 토큰으로 사용자 정보 요청
//        Map<String, Object> userAttributes = kakaoService.getKakaoUserInfo(accessToken);

        // OAuthAttributes 객체 생성
//        OAuthAttributes oAuthAttributes = OAuthAttributes.of("kakao", "id", userAttributes);

        // 사용자 정보로 회원가입 또는 로그인 처리
        Member member = authService.findOrCreateMember(Provider.KAKAO, email);

        // JWT 토큰을 클라이언트에 반환
        JwtToken jwtToken = jwtTokenProvider.generateJwtToken(member);
        MemberResponseDto.LoginResponseDto loginResponse = AuthConverter.toLoginDto(member, jwtToken);

        return ApiResponse.onSuccess(loginResponse);
    }

//    @PostMapping("auth/apple/login")
//    @Operation(summary = "애플 로그인", description = "애플 로그인 후 액세스 토큰을 사용하여 로그인합니다.")
//    public ApiResponse<MemberResponseDto.LoginResponseDto> appleLogin(@RequestBody KakaoLoginRequestDto requestDto) {
//        String accessToken = requestDto.getAccessToken();
//        MemberResponseDto.LoginResponseDto loginResponse = authService.handleOAuth2Callback(email);
//        return ApiResponse.onSuccess(loginResponse);
//    }

//    @GetMapping("/login/oauth2/code/{providerId}")
//    public void handleOAuth2Login(
//            @PathVariable String providerId,
//            @RequestParam String code,
//            HttpServletResponse response) throws IOException {
//
//        // OAuth2 인증 코드로 액세스 토큰을 요청
//        String accessToken = authService.getAccessToken(providerId, code);
//
//        // 액세스 토큰으로 사용자 정보를 요청
//        Map<String, Object> userAttributes = authService.getUserInfo(providerId, accessToken);
//
//        // OAuthAttributes 객체 생성
//        OAuthAttributes oAuthAttributes = OAuthAttributes.of(providerId, "id", userAttributes);
//
//        // 사용자 정보로 회원가입 또는 로그인 처리
//        Member member = authService.findOrCreateMember(Provider.valueOf(providerId.toUpperCase()), providerId, oAuthAttributes.getEmail());
//
//        // JWT 토큰을 생성하고 클라이언트로 리디렉션
//        JwtToken jwtToken = jwtTokenProvider.generateJwtToken(member);
//        String loginUrl = "http://localhost:8080/api/auth/login?token=" + jwtToken.getAccessToken();
//
//        // 클라이언트에 리디렉션
//        response.sendRedirect(loginUrl);
//    }

    @PostMapping("/logout")
    @Operation(summary = "로그아웃", description = "로그인한 사용자가 로그아웃 처리됩니다.")
    public ApiResponse<?> logout() {
        // 현재 사용자 인증 정보 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()) {
            // 현재 사용자의 인증 정보 무효화
            SecurityContextHolder.getContext().setAuthentication(null);
        }
        authService.logOut(authentication.getName());
        return ApiResponse.onSuccess("로그아웃되었습니다.", authentication.getName());
    }
}