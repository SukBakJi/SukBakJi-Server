package umc.SukBakJi.domain.board.service;

import umc.SukBakJi.domain.block.repository.BlockRepository;
import umc.SukBakJi.domain.board.model.dto.*;
import umc.SukBakJi.domain.board.model.entity.Board;
import umc.SukBakJi.domain.board.model.entity.Comment;
import umc.SukBakJi.domain.member.model.entity.Member;
import umc.SukBakJi.domain.board.model.entity.Post;
import umc.SukBakJi.domain.common.entity.enums.Menu;
import umc.SukBakJi.domain.board.repository.BoardRepository;
import umc.SukBakJi.domain.member.repository.MemberRepository;
import umc.SukBakJi.domain.board.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import umc.SukBakJi.global.apiPayload.code.status.ErrorStatus;
import umc.SukBakJi.global.apiPayload.exception.GeneralException;
import umc.SukBakJi.global.filter.BadWordFilter;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class PostService {

    private final PostRepository postRepository;
    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;

    private final BlockRepository blockRepository;
    private final BadWordFilter badWordFilter;

    @Autowired
    public PostService(PostRepository postRepository, BoardRepository boardRepository, MemberRepository memberRepository, BlockRepository blockRepository, BadWordFilter badWordFilter) {
        this.postRepository = postRepository;
        this.boardRepository = boardRepository;
        this.memberRepository = memberRepository;
        this.blockRepository = blockRepository;
        this.badWordFilter = badWordFilter;
    }

    public PostResponseDTO createPost(CreatePostRequestDTO request, Long memberId) {

        // 🔒 비속어 검사
        if (badWordFilter.containsBadWord(request.getTitle())) {
            throw new GeneralException(ErrorStatus.BAD_WORD_DETECTED);
        }
        if (badWordFilter.containsBadWord(request.getContent())) {
            throw new GeneralException(ErrorStatus.BAD_WORD_DETECTED);
        }

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

    public PostDetailResponseDTO getPostDetail(Long postId, Long currentMemberId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.POST_NOT_FOUND));

        // Increment the view count
        post.setViews(post.getViews() + 1);
        postRepository.save(post);

        // Set hot timestamp if needed
        if (post.getViews() >= 100 && post.getHotTimestamp() == null) {
            post.setHotTimestamp(LocalDateTime.now());
            postRepository.save(post);
        }

        return convertToPostDetailResponseDTO(post, currentMemberId);
    }

    private PostDetailResponseDTO convertToPostDetailResponseDTO(Post post, Long currentMemberId) {
        PostDetailResponseDTO response = new PostDetailResponseDTO();
        response.setMenu(post.getBoard().getMenu().name());
        response.setTitle(post.getTitle());
        response.setContent(post.getContent());
        response.setSupportField(post.getSupportField());
        response.setHiringType(post.getHiringType());
        response.setViews(post.getViews());
        response.setCommentCount((long) post.getComments().size());
        response.setMemberId(post.getMember().getId());

        Map<Long, String> memberAnonymousMap = new HashMap<>();
        Set<Integer> usedNumbers = new HashSet<>();

        List<Long> blockedIds = blockRepository.findBlockedIdsByBlockerId(currentMemberId);
        response.setComments(post.getComments().stream()
                .filter(comment -> !blockedIds.contains(comment.getMember().getId()))
                .map(comment -> {
            Long commentMemberId = comment.getMember().getId();

            // 글쓴이 여부 확인
            String anonymousName;
            if (commentMemberId.equals(post.getMember().getId())) {
                anonymousName = "글쓴이";
            } else {
                if (!memberAnonymousMap.containsKey(commentMemberId)) {
                    int nextNumber = 1;
                    while (usedNumbers.contains(nextNumber)) nextNumber++;
                    anonymousName = "익명" + nextNumber;
                    usedNumbers.add(nextNumber);
                    memberAnonymousMap.put(commentMemberId, anonymousName);
                } else {
                    anonymousName = memberAnonymousMap.get(commentMemberId);
                }
            }

            return convertToCommentDTO(comment, anonymousName);
        }).collect(Collectors.toList()));

        return response;
    }


    private PostDetailResponseDTO.CommentDTO convertToCommentDTO(Comment comment, String anonymousName) {
        PostDetailResponseDTO.CommentDTO dto = new PostDetailResponseDTO.CommentDTO();
        dto.setCommentId(comment.getCommentId()); // commentId 추가
        dto.setMemberId(comment.getMember().getId());
        dto.setAnonymousName(anonymousName);
        dto.setDegreeLevel(comment.getMember().getDegreeLevel() != null
                ? comment.getMember().getDegreeLevel().toString()
                : "Unknown");
        dto.setContent(comment.getContent());
        dto.setCreatedDate(comment.getCreatedAt());
        return dto;
    }

    public List<PostListResponseDTO> getPostList(String menu, String boardName, Long currentMemberId) {
        List<Long> blockedIds = blockRepository.findBlockedIdsByBlockerId(currentMemberId);

        List<Post> posts;

        if (blockedIds.isEmpty()) {
            // 차단한 사람이 없으면 그냥 전체 조회
            Board board = boardRepository.findByMenuAndBoardName(Menu.valueOf(menu), boardName)
                    .orElseThrow(() -> new GeneralException(ErrorStatus.INVALID_MENU_OR_BOARD));
            posts = postRepository.findByBoard(board);
        } else {
            posts = postRepository.findByMenuAndBoardNameAndWriterIdNotIn(
                    Menu.valueOf(menu), boardName, blockedIds
            );
        }

        return posts.stream().map(post -> {
            PostListResponseDTO dto = new PostListResponseDTO();
            dto.setPostId(post.getPostId());
            dto.setTitle(post.getTitle());
            dto.setPreviewContent(post.getContent().length() > 30 ? post.getContent().substring(0, 30) + "..." : post.getContent());
            dto.setSupportField(post.getSupportField()); // Setting supportField
            dto.setHiringType(post.getHiringType());     // Setting hiringType
            dto.setCommentCount((long) post.getComments().size());
            dto.setViews(post.getViews());
            return dto;
        }).collect(Collectors.toList());
    }

    public PostResponseDTO updatePost(Long postId, UpdatePostRequestDTO request, Long memberId) {
        // 게시글 존재 여부 확인
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.POST_NOT_FOUND));

        // 게시글 작성자와 요청자가 같은지 확인
        if (!post.getMember().getId().equals(memberId)) {
            throw new GeneralException(ErrorStatus.INVALID_MEMBER_ID);
        }

        // 수정 가능한 필드 업데이트
        if (request.getTitle() != null) post.setTitle(request.getTitle());
        if (request.getContent() != null) post.setContent(request.getContent());
        if (request.getSupportField() != null) post.setSupportField(request.getSupportField());
        if (request.getJob() != null) post.setJob(request.getJob());
        if (request.getHiringType() != null) post.setHiringType(request.getHiringType());
        if (request.getFinalEducation() != null) post.setFinalEducation(request.getFinalEducation());

        // 수정된 게시글 저장
        Post updatedPost = postRepository.save(post);

        // 응답 DTO 생성 및 반환
        PostResponseDTO responseDTO = new PostResponseDTO();
        responseDTO.setPostId(updatedPost.getPostId());
        responseDTO.setTitle(updatedPost.getTitle());
        responseDTO.setContent(updatedPost.getContent());
        responseDTO.setViews(updatedPost.getViews());
        responseDTO.setCreatedAt(updatedPost.getCreatedAt().toString());
        responseDTO.setUpdatedAt(updatedPost.getUpdatedAt().toString());
        responseDTO.setBoardId(updatedPost.getBoard().getBoardId());
        responseDTO.setMemberId(updatedPost.getMember().getId());

        return responseDTO;
    }

    public void deletePost(Long postId, Long memberId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.POST_NOT_FOUND));

        if (!post.getMember().getId().equals(memberId)) {
            throw new GeneralException(ErrorStatus.UNAUTHORIZED_ACCESS);
        }

        postRepository.delete(post);
    }
}
