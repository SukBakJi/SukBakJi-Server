package umc.SukBakJi.domain.board.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import umc.SukBakJi.domain.board.model.entity.PostReport;
import umc.SukBakJi.domain.board.model.entity.Post;
import umc.SukBakJi.domain.member.model.entity.Member;

public interface PostReportRepository extends JpaRepository<PostReport, Long> {
    boolean existsByPostAndMember(Post post, Member member);
}