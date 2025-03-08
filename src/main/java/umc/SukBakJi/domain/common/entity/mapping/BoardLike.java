package umc.SukBakJi.domain.common.entity.mapping;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import umc.SukBakJi.domain.board.model.entity.Board;
import umc.SukBakJi.domain.member.model.entity.Member;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
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

