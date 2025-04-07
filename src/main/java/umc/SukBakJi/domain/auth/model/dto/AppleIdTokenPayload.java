package umc.SukBakJi.domain.auth.model.dto;

import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AppleIdTokenPayload {
    private String sub;
    private String email;
}