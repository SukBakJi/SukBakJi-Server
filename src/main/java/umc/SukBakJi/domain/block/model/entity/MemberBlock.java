package umc.SukBakJi.domain.block.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import umc.SukBakJi.domain.common.entity.BaseEntity;
import umc.SukBakJi.domain.member.model.entity.Member;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberBlock extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 차단을 건 사람
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "blocker_id")
    private Member blocker;

    // 차단 당한 사람
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "blocked_id")
    private Member blocked;


    // 차단 활성 여부 (차단 해제시 false로)
    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }
}
