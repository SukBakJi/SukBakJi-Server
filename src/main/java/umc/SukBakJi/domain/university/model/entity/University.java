package umc.SukBakJi.domain.university.model.entity;

import jakarta.persistence.*;
import lombok.*;
import umc.SukBakJi.domain.common.entity.BaseEntity;

@Entity
@Getter
@Builder
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class University extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name; // 대학교명
}
