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

    private String category;

    @ManyToMany(mappedBy = "researchTopics")
    private List<Lab> labs;

    public ResearchTopic(String topicName, String category) {
        this.topicName = topicName;
        this.category = category;
    }

    public void setTopicName(String researchTopicName) {
        this.topicName = researchTopicName;
    }
}