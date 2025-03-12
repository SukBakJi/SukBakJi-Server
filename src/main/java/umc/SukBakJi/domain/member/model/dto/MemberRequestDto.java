package umc.SukBakJi.domain.member.model.dto;

import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;
import umc.SukBakJi.domain.common.entity.enums.DegreeLevel;
import umc.SukBakJi.domain.common.entity.enums.EducationCertificateType;
import umc.SukBakJi.domain.common.entity.enums.Provider;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberRequestDto {
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
    public static class CertificateDto {
        private EducationCertificateType documentType;
        private MultipartFile certificationPicture;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class searchEmailDto {
        private String name;
        private String phoneNumber;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SearchPasswordDto {
        private String email;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class EmailCodeDto {
        private String email;
        private String code;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ModifyPasswordDto {
        @Pattern(regexp = "^.{6,}$", message = "비밀번호는 여섯 자리 이상 입력해주세요.")
        private String newPassword;
        private String confirmPassword;
    }
}