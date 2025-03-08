package umc.SukBakJi.domain.board.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import umc.SukBakJi.domain.common.entity.mapping.BoardLike;
import umc.SukBakJi.domain.common.entity.BaseEntity;
import umc.SukBakJi.domain.common.entity.enums.Menu;

import java.util.List;

@Entity
@Getter
@Setter
public class Board extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long boardId;

    private String boardName;

    @Column(length = 50)
    private String description;

    @Enumerated(EnumType.STRING)
    private Menu menu;

    // Define relationships if necessary
    @OneToMany(mappedBy = "board")
    private List<Post> posts;

    @OneToMany(mappedBy = "board")
    private List<BoardLike> boardLikes;
}