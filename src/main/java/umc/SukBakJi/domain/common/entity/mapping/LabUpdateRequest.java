package umc.SukBakJi.domain.common.entity.mapping;

import jakarta.persistence.*;
import lombok.*;
import umc.SukBakJi.domain.lab.model.entity.Lab;
import umc.SukBakJi.domain.member.model.entity.Member;
import umc.SukBakJi.domain.common.entity.enums.LabUpdateStatus;
import umc.SukBakJi.domain.common.entity.enums.RequestCategory;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "lab_update_request")
public class LabUpdateRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RequestCategory requestCategory;

    @Column(nullable = false)
    private String content;

    @Enumerated(EnumType.STRING)
    private LabUpdateStatus labUpdateStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lab_id")
    private Lab lab;
}
