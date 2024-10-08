package umc.SukBakJi.domain.model.dto.member;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import umc.SukBakJi.domain.model.entity.enums.DegreeLevel;
import umc.SukBakJi.domain.model.entity.enums.Provider;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberRequestDto {
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class EmailDto {
        @Email(message = "유효하지 않은 이메일 형식입니다.")
        private String email;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class SignUpDto {
        private Provider provider;
        @Email(message = "유효하지 않은 이메일 형식입니다.")
        private String email;
        @Pattern(regexp = "^.{6,}$", message = "비밀번호는 여섯 자리 이상 입력해주세요.")
        private String password;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LoginDto {
        @Email(message = "유효하지 않은 이메일 형식입니다.")
        private String email;
        @Pattern(regexp = "^.{6,}$", message = "비밀번호는 여섯 자리 이상 입력해주세요.")
        private String password;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProfileDto {
        private String name;
        private DegreeLevel degreeLevel;
        private List<String> researchTopics;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ModifyProfileDto {
        private DegreeLevel degreeLevel;
        private List<String> researchTopics;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PasswordDto {
        private String currentPassword;
        @Pattern(regexp = "^.{6,}$", message = "비밀번호는 여섯 자리 이상 입력해주세요.")
        private String newPassword;
        private String confirmPassword;
    }
}