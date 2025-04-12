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
    private final JwtBlacklistService jwtBlacklistService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String uri = request.getRequestURI();

        // 로그인 및 회원가입 관련 요청은 JWT 검증 안함
        if (uri.startsWith("/admin/") ||
                (uri.startsWith("/api/auth/") && !uri.equals("/api/auth/logout")) ||
                uri.startsWith("/api/sms/") ||
                uri.startsWith("/v3/api-docs") ||
                uri.startsWith("/swagger-ui/") ||
                uri.startsWith("/swagger-resources/")
        ) {
            filterChain.doFilter(request, response);
            return;
        }

        // 요청 헤더에서 JWT 토큰 추출
        String token = resolveToken(request);

        // 토큰 유효성 검사
        if (token != null && jwtBlacklistService.isBlacklisted(token)) {
            if (jwtBlacklistService.isBlacklisted(token)) {
                log.warn("블랙 리스트에 등록된 액세스 토큰입니다.");
                handleException(response, HttpServletResponse.SC_UNAUTHORIZED, "이미 로그아웃된 토큰입니다. 다시 로그인해주세요.");
            } else {
                Authentication authentication = jwtTokenProvider.getAuthentication(token);
                SecurityContextHolder.getContext().setAuthentication(authentication);
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