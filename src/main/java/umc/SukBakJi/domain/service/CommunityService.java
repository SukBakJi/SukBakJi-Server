package umc.SukBakJi.domain.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import umc.SukBakJi.domain.converter.PostConverter;
import umc.SukBakJi.domain.model.dto.HotBoardPostDTO;
import umc.SukBakJi.domain.model.dto.LatestQuestionDTO;
import umc.SukBakJi.domain.model.dto.PostListDTO;
import umc.SukBakJi.domain.model.dto.PostSearchDTO;
import umc.SukBakJi.domain.model.entity.mapping.BoardLike;
import umc.SukBakJi.domain.model.entity.Post;
import umc.SukBakJi.domain.model.entity.enums.Menu;
import umc.SukBakJi.domain.repository.BoardLikeRepository;
import umc.SukBakJi.domain.repository.PostRepository;
import umc.SukBakJi.global.apiPayload.code.status.ErrorStatus;
import umc.SukBakJi.global.apiPayload.exception.GeneralException;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommunityService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private BoardLikeRepository boardLikeRepository;

    // 최신 질문글 목록
    public List<LatestQuestionDTO> getLatestQuestions(){
        List<LatestQuestionDTO> latestQuestions = new ArrayList<>();

        for (Menu menu : Menu.values()) {
            List<Post> posts = postRepository.findTopByBoardMenuOrderByCreatedAtDesc(menu);
            if (!posts.isEmpty()) {
                Post latestPost = posts.get(0);
                latestQuestions.add(PostConverter.toLatestQuestionDTO(latestPost));
            }
        }

        if (latestQuestions.isEmpty()) {
            throw new GeneralException(ErrorStatus.POST_NOT_FOUND);
        }

        return latestQuestions;
    }

    // HOT 게시글 목록
    public List<HotBoardPostDTO> getHotBoardPosts() {
        List<Post> hotPosts = postRepository.findHotPosts();
        return hotPosts.stream()
                .map(PostConverter::toHotBoardPostDTO)
                .collect(Collectors.toList());
    }

    // 특정 사용자가 스크랩한 게시글 목록
    public List<PostListDTO> getScrappedPostsByUserId(Long userId) {
        return postRepository.findScrappedPostsByMemberId(userId).stream()
                .map(PostConverter::toPostListDTO)
                .collect(Collectors.toList());
    }

    // 특정 사용자가 작성한 게시글 목록
    public List<PostListDTO> getPostsByUserId(Long userId) {
        List<Post> posts = postRepository.findByMemberId(userId);
        if (posts.isEmpty()) {
            throw new GeneralException(ErrorStatus.POST_NOT_FOUND);
        }
        return posts.stream()
                .map(PostConverter::toPostListDTO)
                .collect(Collectors.toList());
    }

    // 특정 사용자가 작성한 댓글의 게시글 목록
    public List<PostListDTO> getCommentedPostsByUserId(Long userId) {
        List<Post> posts = postRepository.findPostsByMemberComments(userId);
        if (posts.isEmpty()) {
            throw new GeneralException(ErrorStatus.NO_COMMENTS_FOUND);
        }
        return posts.stream()
                .map(PostConverter::toPostListDTO)
                .collect(Collectors.toList());
    }

    // 특정 사용자가 즐겨찾기한 게시판의 최신 게시글 목록
    public List<PostListDTO> getFavoritePosts(Long userId) {
        List<BoardLike> favorites = boardLikeRepository.findByMemberId(userId);

        return favorites.stream()
                .map(favorite -> {
                    List<Post> latestPosts = postRepository.findTop1ByBoardIdOrderByCreatedAtDesc(favorite.getBoard().getBoardId());
                    if (!latestPosts.isEmpty()) {
                        return PostConverter.toPostListDTO(latestPosts.get(0));
                    } else {
                        return null;
                    }
                })
                .filter(dto -> dto != null)
                .collect(Collectors.toList());
    }

    public List<PostSearchDTO> searchPosts(String keyword, Menu menu, String boardName) {
        List<Post> posts;

        if (menu != null && boardName != null) {
            // 특정 메뉴와 게시판 이름에서 검색
            posts = postRepository.searchPostsByMenuAndBoardName(menu, boardName, keyword);
        } else {
            // 전체 게시판에서 검색
            posts = postRepository.searchAllPosts(keyword);
        }

        if (posts.isEmpty()) {
            throw new GeneralException(ErrorStatus.POST_NOT_FOUND);
        }

        return posts.stream()
                .map(PostConverter::toPostSearchDTO)
                .collect(Collectors.toList());
    }
}
