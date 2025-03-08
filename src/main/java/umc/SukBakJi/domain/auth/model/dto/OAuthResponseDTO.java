package umc.SukBakJi.domain.auth.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import umc.SukBakJi.domain.common.entity.enums.Provider;

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