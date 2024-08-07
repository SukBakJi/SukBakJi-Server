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

    private String startDate; // 일정 시작 날짜

    private String endDate; // 일정 종료 날짜

    @Column(nullable = false)
    private String season; // 모집시기

    @Column(nullable = false)
    private String method; // 모집전형

    @Column(nullable = false)
    private Long universityId; // fk 지정 시, 데이터 입력 불가능
}
