package umc.SukBakJi.domain.member.model.entity;

import jakarta.persistence.*;
import lombok.*;
import umc.SukBakJi.domain.board.model.entity.Comment;
import umc.SukBakJi.domain.board.model.entity.Post;
import umc.SukBakJi.domain.board.model.entity.Reply;
import umc.SukBakJi.domain.common.entity.enums.DegreeLevel;
import umc.SukBakJi.domain.common.entity.enums.Provider;
import umc.SukBakJi.domain.common.entity.enums.Role;
import umc.SukBakJi.domain.common.entity.enums.UpdateStatus;
import umc.SukBakJi.domain.common.entity.mapping.BoardLike;
import umc.SukBakJi.domain.common.entity.mapping.FavoriteLab;
import umc.SukBakJi.domain.common.entity.mapping.MemberResearchTopic;
import umc.SukBakJi.domain.common.entity.BaseEntity;
import umc.SukBakJi.domain.common.entity.mapping.Scrap;

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

    @Column(nullable = false)
    private String email;

    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    private DegreeLevel degreeLevel;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Provider provider;

    private String providerId;

    private String sub;

    @Enumerated(EnumType.STRING)
    private UpdateStatus educationVerificationStatus;

    private String fcmToken;

    @Builder
    public Member(Provider provider, String providerId) {
        this.provider = provider;
        this.providerId = providerId;
    }

    private String refreshToken;

    private Long labId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
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

    @OneToOne
    @JoinColumn(name = "image_id")
    private Image educationCertificateImage;

    public Member(String name, String password, String email, String phoneNumber, DegreeLevel degreeLevel, Provider provider) {
        this.name = name;
        this.password = password;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.degreeLevel = degreeLevel;
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

    public Long getMemberId() {
        return id;
    }
}

