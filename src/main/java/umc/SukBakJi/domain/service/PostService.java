package umc.SukBakJi.domain.service;

import umc.SukBakJi.domain.model.dto.CreateJobPostRequestDTO;
import umc.SukBakJi.domain.model.dto.CreatePostRequestDTO;
import umc.SukBakJi.domain.model.dto.PostDetailResponseDTO;
import umc.SukBakJi.domain.model.dto.PostListResponseDTO;
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

    public Post createPost(CreatePostRequestDTO request, Long memberId) {
        Board board = boardRepository.findByMenuAndBoardName(request.getMenu(), request.getBoardName())
                .orElseThrow(() -> new IllegalArgumentException("Invalid menu or board name"));

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid member ID"));

        Post post = new Post();
        post.setTitle(request.getTitle());
        post.setContent(request.getContent());
        post.setBoard(board);
        post.setMember(member);
        post.setViews(0L);

        return postRepository.save(post);
    }

    public Post createJobPost(CreateJobPostRequestDTO request, Long memberId) {
        Board board = boardRepository.findByMenuAndBoardName(request.getMenu(), request.getBoardName())
                .orElseThrow(() -> new IllegalArgumentException("Invalid menu or board name"));

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid member ID"));

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

        return postRepository.save(post);
    }

    public PostDetailResponseDTO getPostDetails(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid post ID"));

        // Increment the view count
        post.setViews(post.getViews() + 1);
        postRepository.save(post);

        // Map post to PostDetailResponseDTO
        PostDetailResponseDTO responseDTO = new PostDetailResponseDTO();
        responseDTO.setMenu(post.getBoard().getMenu().name());  // Convert enum to string
        responseDTO.setTitle(post.getTitle());
        responseDTO.setContent(post.getContent());
        responseDTO.setViews(post.getViews());

        List<PostDetailResponseDTO.CommentDTO> commentDTOs = post.getComments().stream()
                .map(comment -> {
                    PostDetailResponseDTO.CommentDTO commentDTO = new PostDetailResponseDTO.CommentDTO();
                    commentDTO.setAnonymousName(comment.getNickname());
                    commentDTO.setDegreeLevel(comment.getMember().getDegreeLevel().name());
                    commentDTO.setContent(comment.getContent());
                    commentDTO.setCreatedDate(comment.getCreatedAt());
                    return commentDTO;
                })
                .collect(Collectors.toList());

        responseDTO.setComments(commentDTOs);
        responseDTO.setCommentCount((long) commentDTOs.size());

        return responseDTO;
    }

    public List<PostListResponseDTO> getPostList(String menu, String boardName) {
        Board board = boardRepository.findByMenuAndBoardName(Menu.valueOf(menu), boardName)
                .orElseThrow(() -> new IllegalArgumentException("Invalid menu or board name"));

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
