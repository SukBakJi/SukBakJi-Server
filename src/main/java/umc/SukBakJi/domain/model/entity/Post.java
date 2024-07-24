package umc.SukBakJi.domain.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import umc.SukBakJi.global.entity.BaseEntity;

import java.util.List;

@Entity
@Getter
@Setter
public class Post extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postId;

    private String title;
    private String content;
    private Long views;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "board_id")
    private Board board;

    @OneToMany(mappedBy = "post")
    private List<Comment> comments;

    @OneToMany(mappedBy = "post")
    private List<Scrap> scraps;
}

