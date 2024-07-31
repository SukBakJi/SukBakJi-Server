package umc.SukBakJi.domain.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import umc.SukBakJi.domain.model.dto.CreateCommentRequestDTO;
import umc.SukBakJi.domain.model.entity.Comment;
import umc.SukBakJi.domain.model.entity.Member;
import umc.SukBakJi.domain.model.entity.Post;
import umc.SukBakJi.domain.repository.CommentRepository;
import umc.SukBakJi.domain.repository.MemberRepository;
import umc.SukBakJi.domain.repository.PostRepository;

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

    public Comment createComment(CreateCommentRequestDTO request) {
        Post post = postRepository.findById(request.getPostId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid post ID"));

        Member member = memberRepository.findById(request.getMemberId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid member ID"));

        // Determine if the commenter is the author of the post
        boolean isAuthor = post.getMember().equals(member);

        String nickname;
        if (isAuthor) {
            nickname = "글쓴이";
        } else {
            // Get all comments for the post
            List<Comment> postComments = commentRepository.findByPost(post);

            // Find existing nickname for the member if exists
            Optional<String> existingNickname = postComments.stream()
                    .filter(comment -> comment.getMember().equals(member))
                    .map(Comment::getNickname)
                    .findFirst();

            if (existingNickname.isPresent()) {
                nickname = existingNickname.get();
            } else {
                // Get the highest nickname number used so far
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
        comment.setNickname(nickname); // Set the nickname

        return commentRepository.save(comment);
    }
}
