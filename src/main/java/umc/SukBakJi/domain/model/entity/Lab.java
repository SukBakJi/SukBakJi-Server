package umc.SukBakJi.domain.model.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import umc.SukBakJi.global.entity.BaseEntity;

import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class Lab extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String labName; // 연구실명

    @Column(nullable = false)
    private String universityName; // 대학명

    @Column(nullable = false)
    private String professorName; // 교수명

    @Column(nullable = false)
    private String department; // 학과명 (Department Name)

    @Column(nullable = true)
    private String professorProfile; // 교수 프로필

    @Column(nullable = true)
    private String professorAcademic; // 교수 학적

    private String labLink; // 연구실 링크

    @ManyToMany
    @JoinTable(
            name = "lab_research_topic",
            joinColumns = @JoinColumn(name = "lab_id"),
            inverseJoinColumns = @JoinColumn(name = "research_topic_id")
    )
    private List<ResearchTopic> researchTopics;


    public Lab(String labName, String universityName, String professorName, String professorProfile, String professorAcademic, String labLink, List<ResearchTopic> researchTopics) {
        this.labName = labName;
        this.universityName = universityName;
        this.professorName = professorName;
        this.department = department;
        this.professorProfile = professorProfile;
        this.professorAcademic = professorAcademic;
        this.labLink = labLink;
        this.researchTopics = researchTopics;
    }
}
