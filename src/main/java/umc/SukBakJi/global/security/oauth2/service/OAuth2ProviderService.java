package umc.SukBakJi.global.security.oauth2.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OAuth2ProviderService {
//    private final KakaoService kakaoService;
//    private final AppleService appleService;
//
//    private OAuth2UserInfo getOAuth2UserInfo(Provider provider, Map<String, Object> attributes) {
//        return switch (provider) {
//            case KAKAO -> new KakaoUserInfo(attributes);
//            case APPLE -> new AppleUserInfo(attributes);
//            default -> throw new IllegalArgumentException("지원하지 않는 로그인 방식: " + provider);
//        };
//    }
//
//    public OAuthAttributes getAccessToken(String accessToken, Provider provider) {
//        return switch (provider) {
//            case KAKAO -> kakaoService.getAccessToken(accessToken);
//            case APPLE -> appleService.getAccessToken(accessToken);
//            default -> throw new IllegalArgumentException("지원하지 않는 로그인 방식: " + provider);
//        };
//    }
//
}