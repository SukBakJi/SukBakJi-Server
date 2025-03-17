package umc.SukBakJi.global.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import umc.SukBakJi.domain.auth.model.dto.AppleIdTokenPayload;
import umc.SukBakJi.global.security.oauth2.service.AppleTokenValidator;

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
    public static String generateClientSecret(String teamId, String keyId, String clientId, String privateKey) throws Exception {
        PrivateKey key = getPrivateKey(privateKey);

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

    private static PrivateKey getPrivateKey(String privateKey) throws Exception {
        byte[] decodedKey = Base64.getDecoder().decode(privateKey);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(decodedKey);
        KeyFactory keyFactory = KeyFactory.getInstance("EC");
        return keyFactory.generatePrivate(keySpec);
    }
}