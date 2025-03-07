package umc.SukBakJi.domain.board.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import umc.SukBakJi.domain.board.model.entity.Comment;
import umc.SukBakJi.domain.board.model.entity.CommentReport;
import umc.SukBakJi.domain.board.model.entity.Post;
import umc.SukBakJi.domain.board.model.entity.PostReport;
import umc.SukBakJi.domain.board.repository.CommentReportRepository;
import umc.SukBakJi.domain.board.repository.CommentRepository;
import umc.SukBakJi.domain.board.repository.PostReportRepository;
import umc.SukBakJi.domain.board.repository.PostRepository;
import umc.SukBakJi.domain.member.model.entity.Member;
import umc.SukBakJi.domain.member.repository.MemberRepository;
import umc.SukBakJi.domain.board.model.dto.ReportCommentRequestDTO;
import umc.SukBakJi.domain.board.model.dto.ReportPostRequestDTO;
import umc.SukBakJi.global.apiPayload.code.status.ErrorStatus;
import umc.SukBakJi.global.apiPayload.exception.GeneralException;

@Service
public class ReportService {

    private final CommentReportRepository commentReportRepository;
    private final PostReportRepository postReportRepository;
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final MemberRepository memberRepository;

    @Autowired
    public ReportService(CommentReportRepository commentReportRepository, PostReportRepository postReportRepository,
                         CommentRepository commentRepository, PostRepository postRepository, MemberRepository memberRepository) {
        this.commentReportRepository = commentReportRepository;
        this.postReportRepository = postReportRepository;
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
        this.memberRepository = memberRepository;
    }

    public void reportComment(ReportCommentRequestDTO request, Long memberId) {
        Comment comment = commentRepository.findById(request.getCommentId())
                .orElseThrow(() -> new GeneralException(ErrorStatus.COMMENT_NOT_FOUND));

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.MEMBER_NOT_FOUND));

        if (commentReportRepository.existsByCommentAndMember(comment, member)) {
            throw new GeneralException(ErrorStatus.ALREADY_REPORTED);
        }

        CommentReport report = new CommentReport();
        report.setComment(comment);
        report.setMember(member);
        report.setReason(request.getReason());

        commentReportRepository.save(report);
    }

    public void reportPost(ReportPostRequestDTO request, Long memberId) {
        Post post = postRepository.findById(request.getPostId())
                .orElseThrow(() -> new GeneralException(ErrorStatus.POST_NOT_FOUND));

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.MEMBER_NOT_FOUND));

        if (postReportRepository.existsByPostAndMember(post, member)) {
            throw new GeneralException(ErrorStatus.ALREADY_REPORTED);
        }

        PostReport report = new PostReport();
        report.setPost(post);
        report.setMember(member);
        report.setReason(request.getReason());

        postReportRepository.save(report);
    }
}