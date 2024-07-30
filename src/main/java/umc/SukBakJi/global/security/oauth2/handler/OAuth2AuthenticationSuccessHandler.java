package umc.SukBakJi.global.security.oauth2.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
@Component
public class OAuth2AuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final String kakaoLoginRedirectUri = "http://localhost:8080/auth/login/oauth2/code/kakao";

    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
        log.info("소셜 로그인에 성공했습니다.");

        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        log.info("OAuth2User attributes = {}", new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(oAuth2User.getAttributes()));

        // 사용자 정보를 기반으로 처리하는 리디렉션 url
//        String redirectUri = UriComponentsBuilder.fromUriString(kakaoLoginRedirectUri)
//                .queryParam("code", code)
//                .build()
//                .toUriString();

//        httpServletResponse.sendRedirect(kakaoLoginRedirectUri);
    }
}