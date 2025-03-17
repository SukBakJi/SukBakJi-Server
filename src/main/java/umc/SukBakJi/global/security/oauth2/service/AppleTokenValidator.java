package umc.SukBakJi.global.security.oauth2.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.math.BigInteger;
import java.net.URL;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.RSAPublicKeySpec;
import java.util.Base64;
import java.util.Iterator;

@Service
public class AppleTokenValidator {
    private static final String APPLE_PUBLIC_KEYS_URL = "https://appleid.apple.com/auth/keys";

    // 애플 공개 키 가져오기
    public PublicKey getApplePublicKey(String idToken) throws Exception {
        // ID Token의 헤더에서 kid 값 추출
        String[] tokenParts = idToken.split("\\.");
        if (tokenParts.length < 2) {
            throw new IllegalArgumentException("Invalid ID Token format");
        }

        // JWT 헤더 디코딩
        String headerJson = new String(Base64.getUrlDecoder().decode(tokenParts[0]));
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode header = objectMapper.readTree(headerJson);
        String kid = header.get("kid").asText();

        // 애플 공개 키 가져오기
        InputStream inputStream = new URL(APPLE_PUBLIC_KEYS_URL).openStream();
        JsonNode publicKeys = objectMapper.readTree(inputStream).get("keys");

        // kid 값이 일치하는 공개 키 찾기
        Iterator<JsonNode> elements = publicKeys.elements();
        JsonNode matchingKey = null;
        while (elements.hasNext()) {
            JsonNode key = elements.next();
            if (kid.equals(key.get("kid").asText())) {
                matchingKey = key;
                break;
            }
        }

        if (matchingKey == null) {
            throw new IllegalArgumentException("No matching key found for kid: " + kid);
        }

        // 공개 키 값 변환하여 RSAPublicKey 생성
        byte[] modulusBytes = Base64.getUrlDecoder().decode(matchingKey.get("n").asText());
        byte[] exponentBytes = Base64.getUrlDecoder().decode(matchingKey.get("e").asText());

        BigInteger modulus = new BigInteger(1, modulusBytes);
        BigInteger exponent = new BigInteger(1, exponentBytes);

        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        RSAPublicKeySpec publicKeySpec = new RSAPublicKeySpec(modulus, exponent);
        return (RSAPublicKey) keyFactory.generatePublic(publicKeySpec);
    }
}