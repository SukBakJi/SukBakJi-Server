package umc.SukBakJi.domain.model.entity.mapping;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import umc.SukBakJi.global.entity.BaseEntity;
import umc.SukBakJi.domain.model.entity.Lab;
import umc.SukBakJi.domain.model.entity.Member;
import umc.SukBakJi.domain.model.entity.enums.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class LabReview extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public LabReview(Lab lab, Member member, String content, Atmosphere atmosphere, ThesisGuidance thesisGuidance, LeadershipStyle leadershipStyle, SalaryLevel salaryLevel, GraduationDifficulty graduationDifficulty) {
        this.lab = lab;
        this.member = member;
        this.content = content;
        this.atmosphere = atmosphere;
        this.thesisGuidance = thesisGuidance;
        this.leadershipStyle = leadershipStyle;
        this.salaryLevel = salaryLevel;
        this.graduationDifficulty = graduationDifficulty;
    }

    @ManyToOne
    @JoinColumn(name = "lab_id", nullable = false)
    private Lab lab;

    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Column(nullable = false)
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Atmosphere atmosphere;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ThesisGuidance thesisGuidance;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LeadershipStyle leadershipStyle;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SalaryLevel salaryLevel;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private GraduationDifficulty graduationDifficulty;

    public void updateReview(String content, Atmosphere atmosphere, ThesisGuidance thesisGuidance, LeadershipStyle leadershipStyle, SalaryLevel salaryLevel, GraduationDifficulty graduationDifficulty) {
        this.content = content;
        this.atmosphere = atmosphere;
        this.thesisGuidance = thesisGuidance;
        this.leadershipStyle = leadershipStyle;
        this.salaryLevel = salaryLevel;
        this.graduationDifficulty = graduationDifficulty;
    }

}
