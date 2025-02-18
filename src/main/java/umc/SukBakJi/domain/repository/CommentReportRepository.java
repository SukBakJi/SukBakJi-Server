package umc.SukBakJi.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import umc.SukBakJi.domain.model.entity.CommentReport;
import umc.SukBakJi.domain.model.entity.Comment;
import umc.SukBakJi.domain.model.entity.Member;

import java.util.Optional;

public interface CommentReportRepository extends JpaRepository<CommentReport, Long> {
    boolean existsByCommentAndMember(Comment comment, Member member);
}