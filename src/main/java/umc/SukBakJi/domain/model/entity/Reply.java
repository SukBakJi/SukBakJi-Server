package umc.SukBakJi.domain.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import umc.SukBakJi.global.entity.BaseEntity;

@Entity
@Getter
@Setter
public class Reply extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long coCommentId;

    private String content;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "comment_id")
    private Comment comment;
}
