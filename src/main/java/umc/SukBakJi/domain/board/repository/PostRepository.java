package umc.SukBakJi.domain.board.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import umc.SukBakJi.domain.board.model.entity.Board;
import umc.SukBakJi.domain.board.model.entity.Post;
import umc.SukBakJi.domain.common.entity.enums.Menu;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByBoard(Board board);

    // 최신 질문글 조회
    @Query("SELECT p FROM Post p WHERE p.board.menu = :menu AND p.board.boardName = '질문 게시판' ORDER BY p.createdAt DESC")
    List<Post> findTopByBoardMenuOrderByCreatedAtDesc(@Param("menu") Menu menu, Pageable pageable);
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

    // 특정 메뉴와 게시판 이름에서 검색
    @Query("SELECT p FROM Post p WHERE p.board.menu = :menu AND p.board.boardName = :boardName AND (p.title LIKE %:keyword% OR p.content LIKE %:keyword%)")
    List<Post> searchPostsByMenuAndBoardName(@Param("menu") Menu menu, @Param("boardName") String boardName, @Param("keyword") String keyword);

    // 전체 게시판에서 검색
    @Query("SELECT p FROM Post p WHERE p.title LIKE %:keyword% OR p.content LIKE %:keyword%")
    List<Post> searchAllPosts(@Param("keyword") String keyword);

    @Query("SELECT p FROM Post p WHERE p.board.menu IN :menu AND p.board.menu <> '자유' AND p.board.boardName = '질문 게시판' ORDER BY p.createdAt DESC")
    List<Post> findByBoardMenuAndBoardNameOrderByCreatedAtDesc(@Param("menu") Menu menu, Pageable pageable);

}
