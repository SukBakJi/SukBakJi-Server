package umc.SukBakJi.domain.repository;

import umc.SukBakJi.domain.model.entity.Board;
import umc.SukBakJi.domain.model.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByBoard(Board board);
}
