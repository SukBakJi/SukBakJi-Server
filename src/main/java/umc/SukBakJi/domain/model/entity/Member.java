package umc.SukBakJi.domain.model.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import umc.SukBakJi.global.entity.BaseEntity;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED) // 기본 생성자의 접근 수준을 protected로 설정
@ToString
public class Member extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String phoneNumber;

    @Column(nullable = false)
    private String degreeLevel;

    @Column(nullable = false)
    private int point;

    @Column(nullable = false)
    private String socialType;

    public Member(String name, String password, String email, String phoneNumber, String degreeLevel, int point, String socialType) {
        this.name = name;
        this.password = password;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.degreeLevel = degreeLevel;
        this.point = point;
        this.socialType = socialType;
    }
}