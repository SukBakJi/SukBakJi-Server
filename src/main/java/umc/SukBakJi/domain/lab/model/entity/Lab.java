package umc.SukBakJi.domain.lab.model.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import umc.SukBakJi.domain.common.entity.mapping.LabResearchTopic;
import umc.SukBakJi.domain.common.entity.BaseEntity;

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
    private String labName; // 연구실명 (oo학교 oo학과 oo교수)

    @Column(nullable = false)
    private String universityName; // 대학명

    @Column(nullable = false)
    private String professorName; // 교수명

    @Column(nullable = false)
    private String departmentName; // 학과명

    private String professorEmail; // 교수 이메일

    private String labLink; // 연구실 링크

    @OneToMany(mappedBy = "lab", fetch = FetchType.LAZY)
    private List<LabResearchTopic> labResearchTopics;
}
