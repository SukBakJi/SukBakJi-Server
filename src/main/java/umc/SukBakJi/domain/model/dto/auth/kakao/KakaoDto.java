package umc.SukBakJi.domain.model.dto.auth.kakao;

import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class KakaoDto {
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class KakaoRequestDto {
        private String email;
    }
}
