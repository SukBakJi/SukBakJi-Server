package umc.SukBakJi.global.security.oauth2.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.net.URL;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

@Service
public class AppleTokenValidator {
    private static final String APPLE_PUBLIC_KEYS_URL = "https://appleid.apple.com/auth/keys";

    /**
     * ✅ 애플 공개 키 가져오기 & 서명 검증
     */
    public PublicKey getApplePublicKey(String idToken) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        InputStream inputStream = new URL(APPLE_PUBLIC_KEYS_URL).openStream();
        JsonNode publicKeys = objectMapper.readTree(inputStream).get("keys");

        String n = publicKeys.get(0).get("n").asText();
        String e = publicKeys.get(0).get("e").asText();

        byte[] modulusBytes = Base64.getUrlDecoder().decode(n);
        byte[] exponentBytes = Base64.getUrlDecoder().decode(e);

        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePublic(new X509EncodedKeySpec(modulusBytes));
    }
}