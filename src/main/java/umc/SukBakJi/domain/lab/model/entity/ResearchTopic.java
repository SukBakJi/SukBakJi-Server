package umc.SukBakJi.domain.model.entity;

import jakarta.persistence.*;
import lombok.*;
import umc.SukBakJi.domain.lab.model.entity.Lab;
import umc.SukBakJi.global.entity.BaseEntity;

@Entity
@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@ToString
public class ResearchTopic extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String topicName;

    @ManyToOne
    @JoinColumn(name = "lab_id")
    private Lab lab;
}