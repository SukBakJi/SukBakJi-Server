package umc.SukBakJi.domain.converter;

import umc.SukBakJi.domain.model.dto.LatestQuestionDTO;
import umc.SukBakJi.domain.model.entity.Post;

public class PostConverter {
    public static LatestQuestionDTO toLatestQuestionDTO(Post post) {
        return new LatestQuestionDTO(
                post.getBoard().getMenu(),
                post.getTitle()
        );
    }
}
