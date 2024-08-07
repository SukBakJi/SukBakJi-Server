package umc.SukBakJi.domain.model.entity;

import jakarta.persistence.*;
import lombok.*;
import umc.SukBakJi.domain.model.entity.mapping.MemberResearchTopic;
import umc.SukBakJi.global.entity.BaseEntity;

import java.util.List;

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