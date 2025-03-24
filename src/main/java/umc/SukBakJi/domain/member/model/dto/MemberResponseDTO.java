package umc.SukBakJi.domain.member.model.dto;

import lombok.*;
import umc.SukBakJi.domain.common.entity.enums.DegreeLevel;
import umc.SukBakJi.domain.common.entity.enums.Provider;

import java.util.List;

public class MemberResponseDTO {
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LoginResponseDto {
        private Provider provider;
        private String email;
        private String accessToken;
        private String refreshToken;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProfileResultDto {
        private String name;
        private Provider provider;
        private DegreeLevel degreeLevel;
        private List<String> researchTopics;
    }
}