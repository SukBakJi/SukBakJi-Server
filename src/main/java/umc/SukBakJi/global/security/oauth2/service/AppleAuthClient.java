package umc.SukBakJi.global.security.oauth2.service;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;
import umc.SukBakJi.domain.auth.model.dto.AppleTokenResponse;

import java.util.HashMap;
import java.util.Map;

@Component
public class AppleAuthClient {
    private static final String APPLE_AUTH_URL = "https://appleid.apple.com/auth/token";

    public AppleTokenResponse getIdToken(String clientId, String clientSecret, String grantType, String authorizationCode) {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        Map<String, String> body = new HashMap<>();
        body.put("client_id", clientId);
        body.put("client_secret", clientSecret);
        body.put("grant_type", grantType);
        body.put("code", authorizationCode);

        HttpEntity<Map<String, String>> request = new HttpEntity<>(body, headers);

        ResponseEntity<AppleTokenResponse> response = restTemplate.postForEntity(APPLE_AUTH_URL, request, AppleTokenResponse.class);

        return response.getBody();
    }
}