package umc.SukBakJi.global.entity.mapping;

import jakarta.persistence.*;
import lombok.*;
import umc.SukBakJi.domain.lab.model.entity.Lab;
import umc.SukBakJi.domain.lab.model.entity.ResearchTopic;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@ToString
@Table(name = "lab_research_topic")
public class LabResearchTopic {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lab_id", nullable = false)
    private Lab lab;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "research_topic_id", nullable = false)
    private ResearchTopic researchTopic;
}