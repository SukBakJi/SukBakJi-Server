package umc.SukBakJi.domain.board.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import umc.SukBakJi.domain.member.model.entity.Member;
import umc.SukBakJi.domain.common.entity.BaseEntity;

import java.util.List;

@Entity
@Getter
@Setter
public class Comment extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long commentId;

    private String content;

    @Column(nullable = false)
    private String nickname; // Add the nickname field

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    @OneToMany(mappedBy = "comment")
    private List<Reply> replies;
}

