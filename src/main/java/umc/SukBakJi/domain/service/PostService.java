package umc.SukBakJi.domain.service;

import umc.SukBakJi.domain.model.dto.*;
import umc.SukBakJi.domain.model.entity.Board;
import umc.SukBakJi.domain.model.entity.Comment;
import umc.SukBakJi.domain.model.entity.Member;
import umc.SukBakJi.domain.model.entity.Post;
import umc.SukBakJi.domain.model.entity.enums.Menu;
import umc.SukBakJi.domain.repository.BoardRepository;
import umc.SukBakJi.domain.repository.MemberRepository;
import umc.SukBakJi.domain.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import umc.SukBakJi.global.apiPayload.code.status.ErrorStatus;
import umc.SukBakJi.global.apiPayload.exception.GeneralException;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostService {

    private final PostRepository postRepository;
    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;

    @Autowired
    public PostService(PostRepository postRepository, BoardRepository boardRepository, MemberRepository memberRepository) {
        this.postRepository = postRepository;
        this.boardRepository = boardRepository;
        this.memberRepository = memberRepository;
    }

    public PostResponseDTO createPost(CreatePostRequestDTO request, Long memberId) {
        Board board = boardRepository.findByMenuAndBoardName(request.getMenu(), request.getBoardName())
                .orElseThrow(() -> new GeneralException(ErrorStatus.INVALID_MENU_OR_BOARD));

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.INVALID_MEMBER_ID));

        Post post = new Post();
        post.setTitle(request.getTitle());
        post.setContent(request.getContent());
        post.setBoard(board);
        post.setMember(member);
        post.setViews(0L);

        Post savedPost = postRepository.save(post);

        PostResponseDTO responseDTO = new PostResponseDTO();
        responseDTO.setPostId(savedPost.getPostId());
        responseDTO.setTitle(savedPost.getTitle());
        responseDTO.setContent(savedPost.getContent());
        responseDTO.setViews(savedPost.getViews());
        responseDTO.setCreatedAt(savedPost.getCreatedAt().toString());
        responseDTO.setUpdatedAt(savedPost.getUpdatedAt().toString());
        responseDTO.setBoardId(savedPost.getBoard().getBoardId());
        responseDTO.setMemberId(savedPost.getMember().getId());

        return responseDTO;
    }

    public PostResponseDTO createJobPost(CreateJobPostRequestDTO request, Long memberId) {
        Board board = boardRepository.findByMenuAndBoardName(request.getMenu(), request.getBoardName())
                .orElseThrow(() -> new GeneralException(ErrorStatus.INVALID_MENU_OR_BOARD));

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.INVALID_MEMBER_ID));

        Post post = new Post();
        post.setTitle(request.getTitle());
        post.setContent(request.getContent());
        post.setSupportField(request.getSupportField());
        post.setJob(request.getJob());
        post.setHiringType(request.getHiringType());
        post.setFinalEducation(request.getFinalEducation());
        post.setBoard(board);
        post.setMember(member);
        post.setViews(0L);

        Post savedPost = postRepository.save(post);

        PostResponseDTO responseDTO = new PostResponseDTO();
        responseDTO.setPostId(savedPost.getPostId());
        responseDTO.setTitle(savedPost.getTitle());
        responseDTO.setContent(savedPost.getContent());
        responseDTO.setViews(savedPost.getViews());
        responseDTO.setCreatedAt(savedPost.getCreatedAt().toString());
        responseDTO.setUpdatedAt(savedPost.getUpdatedAt().toString());
        responseDTO.setBoardId(savedPost.getBoard().getBoardId());
        responseDTO.setMemberId(savedPost.getMember().getId());

        return responseDTO;
    }


    public PostDetailResponseDTO getPostDetail(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.POST_NOT_FOUND));

        post.setViews(post.getViews() + 1);
        postRepository.save(post);

        if (post.getViews() >= 100 && post.getHotTimestamp() == null) {
            post.setHotTimestamp(LocalDateTime.now());
            postRepository.save(post);
        }

        return convertToPostDetailResponseDTO(post);
    }

    private PostDetailResponseDTO convertToPostDetailResponseDTO(Post post) {
        PostDetailResponseDTO response = new PostDetailResponseDTO();
        response.setMenu(post.getBoard().getMenu().name());
        response.setTitle(post.getTitle());
        response.setContent(post.getContent());
        response.setViews(post.getViews());
        response.setCommentCount((long) post.getComments().size());
        response.setComments(post.getComments().stream().map(comment -> {
            PostDetailResponseDTO.CommentDTO commentDTO = new PostDetailResponseDTO.CommentDTO();
            commentDTO.setAnonymousName(comment.getNickname());
            commentDTO.setDegreeLevel(comment.getMember().getDegreeLevel().name());
            commentDTO.setContent(comment.getContent());
            commentDTO.setCreatedDate(comment.getCreatedAt());
            return commentDTO;
        }).collect(Collectors.toList()));
        return response;
    }

    public List<PostListResponseDTO> getPostList(String menu, String boardName) {
        Board board = boardRepository.findByMenuAndBoardName(Menu.valueOf(menu), boardName)
                .orElseThrow(() -> new GeneralException(ErrorStatus.INVALID_MENU_OR_BOARD));

        List<Post> posts = postRepository.findByBoard(board);
        return posts.stream().map(post -> {
            PostListResponseDTO dto = new PostListResponseDTO();
            dto.setPostId(post.getPostId());
            dto.setTitle(post.getTitle());
            dto.setPreviewContent(post.getContent().length() > 30 ? post.getContent().substring(0, 30) + "..." : post.getContent());
            dto.setCommentCount((long) post.getComments().size());
            dto.setViews(post.getViews());
            return dto;
        }).collect(Collectors.toList());
    }

    private PostDetailResponseDTO.CommentDTO convertToCommentDTO(Comment comment) {
        PostDetailResponseDTO.CommentDTO dto = new PostDetailResponseDTO.CommentDTO();
        dto.setAnonymousName("익명" + (comment.getCommentId() + 1)); // Adjust as needed for actual anonymous naming logic
        dto.setDegreeLevel(comment.getMember().getDegreeLevel().toString());
        dto.setContent(comment.getContent());
        dto.setCreatedDate(comment.getCreatedAt());
        return dto;
    }
}
