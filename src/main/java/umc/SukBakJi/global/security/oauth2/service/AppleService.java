package umc.SukBakJi.global.security.oauth2.service;


import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import umc.SukBakJi.domain.model.entity.enums.Provider;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class AppleService {
    public String getAccessToken(String accessToken) {
        return null;
    }
    public Map<String, Object> getAppleUserInfo(String accessToken) {
        return null;
    }

    @Slf4j
    @Getter
    public static class OAuthAttributes {
        private final Map<String, Object> attributes;
        private final String nameAttributeKey;
        private final Provider provider;
        private final String email;

        @Builder
        public OAuthAttributes(Map<String, Object> attributes, String nameAttributeKey, Provider provider, String email) {
            this.attributes = attributes;
            this.nameAttributeKey = nameAttributeKey;
            this.provider = provider;
            this.email = email;
        }

        @SneakyThrows
        public static OAuthAttributes of(String registrationId, String userNameAttributeName, Map<String, Object> attributes) {
            log.info("userNameAttributeName = {}", new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(userNameAttributeName));
            log.info("attributes = {}", new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(attributes));

            switch (registrationId) {
                case "kakao":
                    return ofKakao(userNameAttributeName, attributes);
                case "apple":
                    return ofApple(userNameAttributeName, attributes);
                default:
                    throw new IllegalArgumentException("지원하지 않는 로그인 방식: " + registrationId);
            }
        }

        private static OAuthAttributes ofKakao(String userNameAttributeName, Map<String, Object> attributes) {
            Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");

            return OAuthAttributes.builder()
                    .email((String) kakaoAccount.get("email"))
                    .provider(Provider.KAKAO)
                    .attributes(attributes)
                    .nameAttributeKey(userNameAttributeName)
                    .build();
        }

        private static OAuthAttributes ofApple(String userNameAttributeName, Map<String, Object> attributes) {

            return OAuthAttributes.builder()
                    .email((String) attributes.get("email"))
                    .provider(Provider.APPLE)
                    .attributes(attributes)
                    .nameAttributeKey(userNameAttributeName)
                    .build();
        }
    }
}
