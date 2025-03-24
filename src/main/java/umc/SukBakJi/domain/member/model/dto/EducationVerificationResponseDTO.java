package umc.SukBakJi.domain.member.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class EducationVerificationResponseDTO {
    private Long memberId;
    private String email;
    private String name;
    private String educationCertificateImageUrl;
}
