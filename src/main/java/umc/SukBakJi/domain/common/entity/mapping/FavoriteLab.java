package umc.SukBakJi.global.entity.mapping;

import jakarta.persistence.*;
import lombok.*;
import umc.SukBakJi.domain.lab.model.entity.Lab;
import umc.SukBakJi.domain.member.model.entity.Member;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class FavoriteLab {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lab_id")
    private Lab lab;
}