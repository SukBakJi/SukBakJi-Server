package umc.SukBakJi.domain.model.dto.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class CertificationDTO {
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class smsRequestDto {
        @NotBlank(message = "전화번호는 필수 입력값입니다.")
        @Pattern(
                regexp = "^01[0-9]\\d{7}$",
                message = "전화번호는 숫자 11자리여야 합니다."
        )
        private String phoneNumber;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class smsVerifyDto {
        private String phoneNumber;
        private String verificationCode;
    }
}