package umc.SukBakJi.domain.common.entity.mapping;

import jakarta.persistence.*;
import lombok.*;
import umc.SukBakJi.domain.common.entity.BaseEntity;
import umc.SukBakJi.domain.member.model.entity.Member;
import umc.SukBakJi.domain.board.model.entity.Post;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Scrap extends BaseEntity {
    @EmbeddedId
    private ScrapId id;

    @ManyToOne
    @MapsId("memberId")
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne
    @MapsId("postId")
    @JoinColumn(name = "post_id")
    private Post post;
}

