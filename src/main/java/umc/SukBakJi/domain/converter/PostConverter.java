package umc.SukBakJi.domain.converter;

import org.springframework.stereotype.Component;
import umc.SukBakJi.domain.model.dto.HotBoardPostDTO;
import umc.SukBakJi.domain.model.dto.LatestQuestionDTO;
import umc.SukBakJi.domain.model.dto.PostListDTO;
import umc.SukBakJi.domain.model.entity.Post;

@Component
public class PostConverter {
    public static LatestQuestionDTO toLatestQuestionDTO(Post post) {
        return new LatestQuestionDTO(
                post.getBoard().getMenu(),
                post.getTitle()
        );
    }

    public static HotBoardPostDTO toHotBoardPostDTO(Post post) {
        return new HotBoardPostDTO(
                post.getBoard().getMenu(),
                post.getBoard().getBoardName(),
                post.getTitle(),
                post.getContent(),
                post.getComments().size(),
                post.getViews()
        );
    }

    public static PostListDTO toPostListDTO(Post post) {
        long commentCount = post.getComments().stream()
                .mapToLong(comment -> 1 + comment.getReplies().size())
                .sum();

        return PostListDTO.builder()
                .postId(post.getPostId())
                .title(post.getTitle())
                .content(post.getContent())
                .views(post.getViews())
                .boardName(post.getBoard().getBoardName())
                .menu(post.getBoard().getMenu().name())
                .commentCount(commentCount)
                .build();
    }
}
