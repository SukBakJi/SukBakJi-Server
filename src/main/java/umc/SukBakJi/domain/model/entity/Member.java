package umc.SukBakJi.domain.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import umc.SukBakJi.domain.model.entity.enums.DegreeLevel;
import umc.SukBakJi.domain.model.entity.enums.Provider;
import umc.SukBakJi.domain.model.entity.mapping.*;
import umc.SukBakJi.domain.model.entity.mapping.MemberResearchTopic;
import umc.SukBakJi.global.entity.BaseEntity;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Member extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    private String name;

    private String password;

    //    @Column(unique = true, nullable = false)
    private String email;

    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    private DegreeLevel degreeLevel;

    @ColumnDefault("0")
    private Integer point;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Provider provider;

    private String providerId;

    @Builder
    public Member(Provider provider, String providerId) {
        this.provider = provider;
        this.providerId = providerId;
    }

    private String refreshToken;

    private Long labId;

    private boolean isEducationVerified;

    // Define relationships if necessary
    @OneToMany(mappedBy = "member")
    private List<MemberResearchTopic> memberResearchTopics;

    @OneToMany(mappedBy = "member")
    private List<FavoriteLab> favoriteLabs;

    @OneToMany(mappedBy = "member")
    private List<Post> posts;

    @OneToMany(mappedBy = "member")
    private List<Comment> comments;

    @OneToMany(mappedBy = "member")
    private List<Reply> replies;

    @OneToMany(mappedBy = "member")
    private List<BoardLike> boardLikes;

    @OneToMany(mappedBy = "member")
    private List<Scrap> scraps;

    public Member(String name, String password, String email, String phoneNumber, DegreeLevel degreeLevel, int point, Provider provider) {
        this.name = name;
        this.password = password;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.degreeLevel = degreeLevel;
        this.point = point;
        this.provider = provider;
    }

    public Member(String email) {
        this.email = email;
    }

    public void updateRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public void resetRefreshToken() {
        this.refreshToken = null;
    }

    @PrePersist
    public void setPoint() {
        this.point = 0;
    }

    public Long getMemberId() {
        return id;
    }
}

