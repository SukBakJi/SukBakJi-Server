package umc.SukBakJi.domain.model.dto.member;

import lombok.*;
import umc.SukBakJi.domain.model.entity.enums.DegreeLevel;
import umc.SukBakJi.domain.model.entity.enums.Provider;
import umc.SukBakJi.domain.model.entity.mapping.MemberResearchTopic;

import java.util.List;

public class MemberResponseDto {
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