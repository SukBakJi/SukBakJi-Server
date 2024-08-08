package umc.SukBakJi.domain.model.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
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

    @Column(columnDefinition = "VARCHAR(255) DEFAULT ''")
    private String date; // 일정 날짜

    @Column(nullable = false)
    private String season; // 모집시기

    @Column(nullable = false)
    private String method; // 모집전형

    @Column(nullable = false)
    private Long universityId; // fk 지정 시, 데이터 입력 불가능
}
