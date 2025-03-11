package umc.SukBakJi.domain.auth.model.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import umc.SukBakJi.domain.common.entity.enums.Provider;

public class AuthRequestDTO {
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
        @Pattern(
                regexp = "^01[0-9]\\d{8}$",
                message = "전화번호는 숫자 11자리여야 합니다."
        )
        private String phoneNumber;
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
}