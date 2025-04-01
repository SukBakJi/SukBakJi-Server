package umc.SukBakJi.domain.member.model.entity;

import jakarta.persistence.*;
import lombok.*;
import umc.SukBakJi.domain.common.entity.BaseEntity;
import umc.SukBakJi.domain.common.entity.enums.EducationCertificateType;

@Entity
@Builder
@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Image extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String uuid;

    @Enumerated(EnumType.STRING)
    private EducationCertificateType type;

    @OneToOne(mappedBy = "educationCertificateImage")
    private Member member;
}