package umc.SukBakJi.domain.board.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import umc.SukBakJi.domain.member.model.entity.Member;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class PostReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reportId;

    @ManyToOne
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Column(nullable = false)
    private String reason;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
}