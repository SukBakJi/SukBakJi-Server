package umc.SukBakJi.domain.model.entity;

import jakarta.persistence.*;
import lombok.*;
import umc.SukBakJi.global.entity.BaseEntity;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class UnivScheduleInfo extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String content; // 일정 내용

    @Column(nullable = false)
    private String date; // 일정 날짜

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "universityId")
    private University university;
}
