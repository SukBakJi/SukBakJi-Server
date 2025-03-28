package umc.SukBakJi.domain.common.entity.mapping;

import jakarta.persistence.*;
import lombok.*;
import umc.SukBakJi.domain.member.model.entity.Member;
import umc.SukBakJi.domain.mentoring.model.entity.Mentor;
import umc.SukBakJi.domain.common.entity.BaseEntity;

@Entity
@Getter
@Builder
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class Mentoring extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mentorId")
    private Mentor mentor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menteeId")
    private Member member;

    @Column(nullable = false)
    private String subject; // 멘토링 주제

    @Column(nullable = false)
    private String question; // 사전 질문
}
