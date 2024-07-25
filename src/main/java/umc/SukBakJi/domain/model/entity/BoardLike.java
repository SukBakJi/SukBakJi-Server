package umc.SukBakJi.domain.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class BoardLike {
    @EmbeddedId
    private BoardLikeId id;

    @ManyToOne
    @MapsId("memberId")
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne
    @MapsId("boardId")
    @JoinColumn(name = "board_id")
    private Board board;
}

@Embeddable
class BoardLikeId implements java.io.Serializable {
    private Long memberId;
    private Long boardId;

    // equals and hashCode
}
