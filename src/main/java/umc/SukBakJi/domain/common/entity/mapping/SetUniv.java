package umc.SukBakJi.domain.common.entity.mapping;

import jakarta.persistence.*;
import lombok.*;
import umc.SukBakJi.domain.member.model.entity.Member;
import umc.SukBakJi.domain.university.model.entity.University;
import umc.SukBakJi.domain.common.entity.BaseEntity;

@Entity
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED) // 기본 생성자의 접근 수준을 protected로 설정
@ToString
public class SetUniv extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "memberId")
    private Member member;

    @Column(nullable = false)
    private String season; // 모집시기

    @Column(nullable = false)
    private String method; // 모집전형

    @Column(columnDefinition = "INTEGER DEFAULT 1")
    private Integer showing; // 일정 보일지 여부 체크

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "universityId")
    private University university;

    public void setShowing(Integer showing){
        this.showing = showing;
    }

    public void updateUnivSchedule(String season, String method) {
        this.season = season;
        this.method = method;
    }
}
