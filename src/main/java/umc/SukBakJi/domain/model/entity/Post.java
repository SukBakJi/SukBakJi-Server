package umc.SukBakJi.domain.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import umc.SukBakJi.global.entity.BaseEntity;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
public class Post extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postId;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    private String supportField;  // Optional
    private String job;           // Optional
    private String hiringType;    // Optional
    private String finalEducation;// Optional

    @Column(nullable = false)
    private Long views;

    private LocalDateTime hotTimestamp;

    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne
    @JoinColumn(name = "board_id", nullable = false)
    private Board board;

    @OneToMany(mappedBy = "post")
    private List<Comment> comments;

    @OneToMany(mappedBy = "post")
    private List<Scrap> scraps;
}
