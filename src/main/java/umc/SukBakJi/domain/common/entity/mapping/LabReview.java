package umc.SukBakJi.domain.common.entity.mapping;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import umc.SukBakJi.domain.common.entity.BaseEntity;
import umc.SukBakJi.domain.common.entity.enums.Autonomy;
import umc.SukBakJi.domain.common.entity.enums.LeadershipStyle;
import umc.SukBakJi.domain.common.entity.enums.SalaryLevel;
import umc.SukBakJi.domain.lab.model.entity.Lab;
import umc.SukBakJi.domain.member.model.entity.Member;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class LabReview extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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
    private LeadershipStyle leadershipStyle;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SalaryLevel salaryLevel;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Autonomy autonomy;

    public LabReview(Lab lab, Member member, String content, LeadershipStyle leadershipStyle, SalaryLevel salaryLevel, Autonomy autonomy) {
        this.lab = lab;
        this.member = member;
        this.content = content;
        this.leadershipStyle = leadershipStyle;
        this.salaryLevel = salaryLevel;
        this.autonomy = autonomy;
    }

    public void updateReview(String content, LeadershipStyle leadershipStyle, SalaryLevel salaryLevel, Autonomy autonomy) {
        this.content = content;
        this.leadershipStyle = leadershipStyle;
        this.salaryLevel = salaryLevel;
        this.autonomy = autonomy;
    }
}