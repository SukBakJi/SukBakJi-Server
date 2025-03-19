package umc.SukBakJi.global.security.oauth2.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;
import umc.SukBakJi.domain.auth.model.dto.AppleTokenResponse;

@Slf4j
@Component
public class AppleAuthClient {
    private static final String APPLE_AUTH_URL = "https://appleid.apple.com/auth/token";

    public AppleTokenResponse getIdToken(String clientId, String clientSecret, String grantType, String authorizationCode) {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("client_id", clientId);
        body.add("client_secret", clientSecret);
        body.add("grant_type", grantType);
        body.add("code", authorizationCode);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);

        try {
            ResponseEntity<String> rawResponse = restTemplate.postForEntity(APPLE_AUTH_URL, request, String.class);

            // JSON 응답을 AppleTokenResponse 객체로 변환
            ObjectMapper objectMapper = new ObjectMapper();
            AppleTokenResponse tokenResponse = objectMapper.readValue(rawResponse.getBody(), AppleTokenResponse.class);

            if (tokenResponse == null || tokenResponse.getIdToken() == null) {
                log.error("AppleTokenResponse 또는 ID Token이 null입니다.");
                throw new RuntimeException("AppleTokenResponse or ID Token is null");
            }
            log.info("애플 인증 성공 - ID Token: {}", tokenResponse.getIdToken());
            return tokenResponse;
        } catch (Exception e) {
            log.error("애플 인증 요청 실패: {}", e.getMessage());
            throw new RuntimeException("애플 인증 요청 실패", e);
        }
    }

}

