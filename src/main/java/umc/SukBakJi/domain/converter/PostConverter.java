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
        int totalCommentCount = post.getComments() != null ? post.getComments().size() : 0;
        if (post.getComments() != null) {
            totalCommentCount += post.getComments().stream()
                    .mapToInt(comment -> comment.getReplies() != null ? comment.getReplies().size() : 0)
                    .sum();
        }

        return HotBoardPostDTO.builder()
                .menu(post.getBoard().getMenu())
                .boardName(post.getBoard().getBoardName())
                .title(post.getTitle())
                .content(post.getContent())
                .commentCount(totalCommentCount)
                .views(post.getViews())
                .build();
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
