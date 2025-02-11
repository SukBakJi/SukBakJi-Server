package umc.SukBakJi.domain.model.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import umc.SukBakJi.domain.model.entity.enums.Provider;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OAuthResponseDTO {
    private String email;
    private String name;
    private Provider provider;
    private String accessToken;
    private String refreshToken;
}