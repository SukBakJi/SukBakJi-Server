package umc.SukBakJi.global.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // 로그인 및 회원가입 관련 요청은 JWT 검증 안함
        String uri = request.getRequestURI();

        if ((uri.startsWith("/api/auth/") && !uri.equals("/api/auth/logout")) ||
                uri.startsWith("/api/sms/") ||
                uri.startsWith("/login/oauth2/code/apple") ||
                uri.startsWith("/v3/api-docs") ||
                uri.startsWith("/swagger-ui/") ||
                uri.startsWith("/swagger-resources/")) {

            filterChain.doFilter(request, response);
            return;
        }

        // 요청 헤더에서 JWT 토큰 추출
        String token = resolveToken(request);

        // 토큰 유효성 검사
        if (token != null) {
            try {
                if (jwtTokenProvider.validateToken(token)) {
                    // 토큰이 유효할 경우 토큰에서 Authentication 객체를 가지고 와서 SecurityContext에 저장
                    Authentication authentication = jwtTokenProvider.getAuthentication(token);
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                } else {
                    throw new JwtException("유효하지 않은 JWT 토큰입니다.");
                }
            } catch (ExpiredJwtException e) {
                handleException(response, HttpServletResponse.SC_UNAUTHORIZED, "토큰이 만료되었습니다.");
                return;
            } catch (JwtException e) {
                handleException(response, HttpServletResponse.SC_UNAUTHORIZED, "유효하지 않은 토큰입니다.");
                return;
            } catch (AuthenticationException e) {
                handleException(response, HttpServletResponse.SC_UNAUTHORIZED, "인증에 실패하였습니다.");
                return;
            }
        } else {
            handleException(response, HttpServletResponse.SC_UNAUTHORIZED, "JWT 토큰이 존재하지 않습니다.");
            return;
        }
        filterChain.doFilter(request, response);
    }

    // 요청 헤더에서 토큰 정보 추출
    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            log.info("Token: " + bearerToken);
            return bearerToken.substring(7);
        }
        return null;
    }

    private void handleException(HttpServletResponse response, int status, String message) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(status);

        // 오류 메시지 객체 생성
        Map<String, String> error = new HashMap<>();
        error.put("status", String.valueOf(status));
        error.put("error", "Unauthorized");
        error.put("message", message);

        // JSON 문자열로 변환
        String errorJson = objectMapper.writeValueAsString(error);

        // 응답 메시지 작성
        response.getWriter().write(errorJson);
    }
}
