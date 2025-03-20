package umc.SukBakJi.domain.alarm.model.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import umc.SukBakJi.domain.member.model.entity.Member;
import umc.SukBakJi.domain.common.entity.BaseEntity;

@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED) // 기본 생성자의 접근 수준을 protected로 설정
@ToString
public class Alarm extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String univName; // 학교 이름

    @Column(nullable = false)
    private String name; // 알람 이름

    @Column(nullable = false)
    private String date; // 알람 날짜

    @Column(nullable = false)
    private String time; // 알람 시간

    @ColumnDefault("1")
    private Long onoff; // 알람 온오프 여부

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "memberId")
    private Member member;

    public void updateAlarm(String name, String univName, String date, String time, Long onoff) {
        this.univName = univName;
        this.name = name;
        this.date = date;
        this.time = time;
        this.onoff = onoff;
    }
}

