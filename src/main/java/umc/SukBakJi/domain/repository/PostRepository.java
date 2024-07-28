package umc.SukBakJi.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import umc.SukBakJi.domain.model.entity.Post;
import umc.SukBakJi.domain.model.entity.enums.Menu;

import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {

    @Query("select p from Post p where p.board.menu = :menu AND p.board.boardName = '질문게시판' ORDER BY p.createdAt DESC ")
    Optional<Post> findTopByBoardMenuOrderByCreatedAtDesc(@Param("menu") Menu menu);
}
