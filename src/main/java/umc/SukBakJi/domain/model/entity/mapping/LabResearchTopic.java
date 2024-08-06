package umc.SukBakJi.domain.model.entity.mapping;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import umc.SukBakJi.domain.model.entity.Lab;
import umc.SukBakJi.domain.model.entity.ResearchTopic;

@Entity
@Getter
@NoArgsConstructor
@ToString
@Table(name = "lab_research_topic")
public class LabResearchTopic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "lab_id", nullable = false)
    private Lab lab;

    @ManyToOne
    @JoinColumn(name = "research_topic_id", nullable = false)
    private ResearchTopic researchTopic;

    public LabResearchTopic(Lab lab, ResearchTopic researchTopic) {
        this.lab = lab;
        this.researchTopic = researchTopic;
    }
}
