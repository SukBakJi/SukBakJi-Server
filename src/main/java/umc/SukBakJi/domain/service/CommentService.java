package umc.SukBakJi.domain.service;

import umc.SukBakJi.domain.model.dto.CommentResponseDTO;
import umc.SukBakJi.domain.model.dto.CreateCommentRequestDTO;
import umc.SukBakJi.domain.model.dto.UpdateCommentRequestDTO;
import umc.SukBakJi.domain.model.entity.Comment;
import umc.SukBakJi.domain.model.entity.Member;
import umc.SukBakJi.domain.model.entity.Post;
import umc.SukBakJi.domain.repository.CommentRepository;
import umc.SukBakJi.domain.repository.MemberRepository;
import umc.SukBakJi.domain.repository.PostRepository;
import umc.SukBakJi.global.apiPayload.code.status.ErrorStatus;
import umc.SukBakJi.global.apiPayload.exception.GeneralException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final MemberRepository memberRepository;

    @Autowired
    public CommentService(CommentRepository commentRepository, PostRepository postRepository, MemberRepository memberRepository) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
        this.memberRepository = memberRepository;
    }

    public CommentResponseDTO createComment(CreateCommentRequestDTO request, Long memberId) {
        Post post = postRepository.findById(request.getPostId())
                .orElseThrow(() -> new GeneralException(ErrorStatus.POST_NOT_FOUND));

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.MEMBER_NOT_FOUND));

        boolean isAuthor = post.getMember().equals(member);
        String nickname;

        if (isAuthor) {
            nickname = "글쓴이";
        } else {
            List<Comment> postComments = commentRepository.findByPost(post);
            Optional<String> existingNickname = postComments.stream()
                    .filter(comment -> comment.getMember().equals(member))
                    .map(Comment::getNickname)
                    .findFirst();

            if (existingNickname.isPresent()) {
                nickname = existingNickname.get();  // Reuse existing anonymous number
            } else {
                Set<Integer> usedNumbers = postComments.stream()
                        .map(Comment::getNickname)
                        .filter(n -> n.startsWith("익명 "))
                        .map(n -> Integer.parseInt(n.split(" ")[1]))
                        .collect(Collectors.toSet());

                int nextNumber = 1;
                while (usedNumbers.contains(nextNumber)) {
                    nextNumber++;
                }
                nickname = "익명 " + nextNumber;
            }
        }

        Comment comment = new Comment();
        comment.setPost(post);
        comment.setMember(member);
        comment.setContent(request.getContent());
        comment.setNickname(nickname);

        Comment savedComment = commentRepository.save(comment);

        // Map the saved comment to CommentResponseDTO
        CommentResponseDTO responseDTO = new CommentResponseDTO();
        responseDTO.setCommentId(savedComment.getCommentId());
        responseDTO.setContent(savedComment.getContent());
        responseDTO.setNickname(savedComment.getNickname());
        responseDTO.setMemberId(savedComment.getMember().getId()); // Set the member ID
        responseDTO.setCreatedAt(savedComment.getCreatedAt());
        responseDTO.setUpdatedAt(savedComment.getUpdatedAt());

        return responseDTO;
    }

    public CommentResponseDTO updateComment(UpdateCommentRequestDTO request, Long memberId) {
        Comment comment = commentRepository.findById(request.getCommentId())
                .orElseThrow(() -> new GeneralException(ErrorStatus.COMMENT_NOT_FOUND));

        // 작성자 확인
        if (!comment.getMember().getId().equals(memberId)) {
            throw new GeneralException(ErrorStatus.UNAUTHORIZED_COMMENT_ACCESS);
        }

        // 내용 수정
        comment.setContent(request.getContent());
        Comment updatedComment = commentRepository.save(comment);

        // 수정된 댓글 정보를 반환
        CommentResponseDTO responseDTO = new CommentResponseDTO();
        responseDTO.setCommentId(updatedComment.getCommentId());
        responseDTO.setContent(updatedComment.getContent());
        responseDTO.setNickname(updatedComment.getNickname());
        responseDTO.setMemberId(updatedComment.getMember().getId());
        responseDTO.setCreatedAt(updatedComment.getCreatedAt());
        responseDTO.setUpdatedAt(updatedComment.getUpdatedAt());

        return responseDTO;
    }
}
