package umc.SukBakJi.domain.repository;

import umc.SukBakJi.domain.model.entity.Board;
import umc.SukBakJi.domain.model.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import umc.SukBakJi.domain.model.entity.enums.Menu;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByBoard(Board board);

    // 최신 질문글 조회
    @Query("SELECT p FROM Post p WHERE p.board.menu = :menu AND p.board.boardName = '질문게시판' ORDER BY p.createdAt DESC")
    List<Post> findTopByBoardMenuOrderByCreatedAtDesc(@Param("menu") Menu menu);
    // HOT 게시글 조회
    @Query("SELECT p FROM Post p WHERE p.hotTimestamp IS NOT NULL ORDER BY p.hotTimestamp DESC")
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

    // 특정 사용자가 즐겨찾기한 게시판의 최신 게시글 조회
    @Query("SELECT p FROM Post p WHERE p.board.boardId = :boardId ORDER BY p.createdAt DESC")
    List<Post> findTop1ByBoardIdOrderByCreatedAtDesc(@Param("boardId") Long boardId);

}