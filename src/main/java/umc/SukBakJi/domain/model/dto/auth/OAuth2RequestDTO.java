package umc.SukBakJi.domain.model.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OAuth2RequestDTO {
    private String provider;
    private String accessToken;
}
