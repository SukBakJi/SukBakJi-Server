package umc.SukBakJi.domain.model.entity;

import jakarta.persistence.*;
import lombok.*;
import umc.SukBakJi.domain.model.entity.enums.DegreeLevel;
import umc.SukBakJi.domain.model.entity.enums.LoginType;
import umc.SukBakJi.global.entity.BaseEntity;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Member extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String password;

    @Column(unique = true, nullable = false)
    private String email;

    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DegreeLevel degreeLevel;

    private Integer point = 0;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LoginType loginType;
}
