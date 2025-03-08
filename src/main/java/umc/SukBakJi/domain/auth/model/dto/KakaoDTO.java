package umc.SukBakJi.domain.model.dto.auth.kakao;

import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class KakaoDTO {
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class KakaoTokenResponse {
        private String access_token;
        private String token_type;
        private String refresh_token;
        private int expires_in;
        private String scope;
        private int refresh_token_expires_in;
    }
}