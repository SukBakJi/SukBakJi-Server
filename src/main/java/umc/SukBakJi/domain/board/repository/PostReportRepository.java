package umc.SukBakJi.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import umc.SukBakJi.domain.model.entity.PostReport;
import umc.SukBakJi.domain.model.entity.Post;
import umc.SukBakJi.domain.model.entity.Member;

public interface PostReportRepository extends JpaRepository<PostReport, Long> {
    boolean existsByPostAndMember(Post post, Member member);
}