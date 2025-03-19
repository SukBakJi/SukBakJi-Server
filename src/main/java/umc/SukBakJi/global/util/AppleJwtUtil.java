package umc.SukBakJi.global.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import umc.SukBakJi.domain.auth.model.dto.AppleIdTokenPayload;
import umc.SukBakJi.global.security.oauth2.service.AppleTokenValidator;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;
import java.util.Date;

@Slf4j
@Component
@RequiredArgsConstructor
public class AppleJwtUtil {

    private final AppleTokenValidator appleTokenValidator;

    // 애플 ID 토큰 검증
    public AppleIdTokenPayload decodeJwt(String identityToken) {
        try {
            // identityToken이 올바른 형식인지 확인
            String[] jwtParts = identityToken.split("\\.");
            if (jwtParts.length != 3) {
                throw new IllegalArgumentException("Invalid JWT format");
            }

            // Base64 URL Safe 방식으로 JWT Payload 디코딩
            String payloadJson = new String(Base64.getUrlDecoder().decode(jwtParts[1]));
            log.info("Decoded JWT Payload: {}", payloadJson);

            // 애플 공개 키를 가져와서 검증
            PublicKey applePublicKey = appleTokenValidator.getApplePublicKey(identityToken);
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(applePublicKey)
                    .build()
                    .parseClaimsJws(identityToken)
                    .getBody();

            String email = claims.get("email", String.class);
            String sub = claims.getSubject();

            return AppleIdTokenPayload.builder()
                    .sub(sub)
                    .email(email)
                    .build();
        } catch (Exception e) {
            log.error("JWT 검증 실패: {}", e.getMessage());
            return null;
        }
    }

    // 애플 OAuth2 client_secret 생성
    public static String generateClientSecret(String teamId, String keyId, String clientId, String privateKeyPath) throws Exception {
        PrivateKey key = getPrivateKey(privateKeyPath);

        return Jwts.builder()
                .setHeaderParam("alg", "ES256")
                .setHeaderParam("kid", keyId)
                .setIssuer(teamId)
                .setAudience("https://appleid.apple.com")
                .setSubject(clientId)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 3600 * 1000))
                .signWith(key, SignatureAlgorithm.ES256)
                .compact();
    }

    private static PrivateKey getPrivateKey(String privateKeyPath) throws Exception {
        try (InputStream inputStream = new FileInputStream(privateKeyPath);
             BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {

            StringBuilder privateKeyContent = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                if (!line.startsWith("-----")) {
                    privateKeyContent.append(line);
                }
            }

            byte[] keyBytes = Base64.getDecoder().decode(privateKeyContent.toString());
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance("EC");
            return keyFactory.generatePrivate(keySpec);
        }
    }
}