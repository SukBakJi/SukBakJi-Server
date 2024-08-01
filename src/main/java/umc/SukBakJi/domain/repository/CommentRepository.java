package umc.SukBakJi.domain.repository;

import umc.SukBakJi.domain.model.entity.Comment;
import umc.SukBakJi.domain.model.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByPost(Post post);
}
