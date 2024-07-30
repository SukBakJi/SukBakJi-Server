package umc.SukBakJi.domain.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.ColumnDefault;
import umc.SukBakJi.domain.model.entity.mapping.BoardLike;
import umc.SukBakJi.domain.model.entity.mapping.Scrap;
import umc.SukBakJi.global.entity.BaseEntity;

import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@ToString
@Setter
public class Member extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
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
    @ColumnDefault("0")
    private Integer point;

    @Column(nullable = false)
    private String socialType;

    // Define relationships if necessary
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
