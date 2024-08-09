package umc.SukBakJi.domain.model.entity.mapping;

import jakarta.persistence.*;
import lombok.*;
import umc.SukBakJi.domain.model.entity.Member;
import umc.SukBakJi.domain.model.entity.Mentor;
import umc.SukBakJi.global.entity.BaseEntity;

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
