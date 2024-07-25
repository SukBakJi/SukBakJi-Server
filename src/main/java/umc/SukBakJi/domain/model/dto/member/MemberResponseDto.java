package umc.SukBakJi.domain.model.dto.member;

import lombok.*;
import umc.SukBakJi.domain.model.entity.enums.DegreeLevel;

import java.util.List;

public class MemberResponseDto {

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SignUpResponseDto {
        private String email;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LoginResponseDto {
        private String email;
        private String accessToken;
        private String refreshToken;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class profileResultDto {
        private String name;
        private DegreeLevel degreeLevel;
        private List<String> researchTopics;
    }
}
