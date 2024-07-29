package umc.SukBakJi.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import umc.SukBakJi.domain.model.entity.Post;
import umc.SukBakJi.domain.model.entity.enums.Menu;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {

    // 최신 질문글 조회
    @Query("SELECT p FROM Post p WHERE p.board.menu = :menu AND p.board.boardName = '질문게시판' ORDER BY p.createdAt DESC")
    List<Post> findTopByBoardMenuOrderByCreatedAtDesc(@Param("menu") Menu menu);
    // HOT 게시글 조회
    @Query("SELECT p FROM Post p WHERE (p.views >= 100 OR (SELECT COUNT(s) FROM Scrap s WHERE s.post = p) >= 20) ORDER BY p.createdAt DESC")
    List<Post> findHotPosts();

    // 특정 사용자가 스크랩한 게시글을 조회
    @Query("SELECT p FROM Post p JOIN p.scraps s WHERE s.member.id = :memberId")
    List<Post> findScrappedPostsByMemberId(@Param("memberId") Long memberId);

    // 특정 사용자가 작성한 게시글 목록 조회
    @Query("SELECT p FROM Post p WHERE p.member.id = :memberId ORDER BY p.createdAt DESC")
    List<Post> findByMemberId(Long memberId);

    // 특정 사용자가 작성한 댓글의 게시글 목록 조회
    @Query("SELECT DISTINCT c.post FROM Comment c WHERE c.member.id = :memberId")
    List<Post> findPostsByMemberComments(@Param("memberId") Long memberId);
}
