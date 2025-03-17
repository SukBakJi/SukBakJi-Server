package umc.SukBakJi.domain.auth.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AppleTokenResponse {
    private String idToken;
    private String accessToken;
    private String refreshToken;
    private String email;
}