package umc.SukBakJi.domain.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import umc.SukBakJi.global.entity.BaseEntity;
import umc.SukBakJi.domain.model.entity.enums.Menu;

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
}