package umc.SukBakJi.domain.common.entity.mapping;

import jakarta.persistence.*;
import lombok.*;
import umc.SukBakJi.domain.member.model.entity.Member;
import umc.SukBakJi.domain.common.entity.enums.LabUpdateStatus;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "lab_review_update_request")
public class LabReviewUpdateRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String content;

    @Enumerated(EnumType.STRING)
    private LabUpdateStatus labUpdateStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "review_id")
    private LabReview labReview;
}
