package umc.SukBakJi.domain.board.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import umc.SukBakJi.domain.board.model.entity.CommentReport;
import umc.SukBakJi.domain.board.model.entity.Comment;
import umc.SukBakJi.domain.member.model.entity.Member;

public interface CommentReportRepository extends JpaRepository<CommentReport, Long> {
    boolean existsByCommentAndMember(Comment comment, Member member);
}