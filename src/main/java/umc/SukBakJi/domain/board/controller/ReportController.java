package umc.SukBakJi.domain.board.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import umc.SukBakJi.domain.board.model.dto.ReportCommentRequestDTO;
import umc.SukBakJi.domain.board.model.dto.ReportPostRequestDTO;
import umc.SukBakJi.domain.board.service.ReportService;
import umc.SukBakJi.global.apiPayload.ApiResponse;
import umc.SukBakJi.global.security.jwt.JwtTokenProvider;

@RestController
@RequestMapping("/api/reports")
public class ReportController {

    private final ReportService reportService;
    private final JwtTokenProvider jwtTokenProvider; // ✅ JWT Provider 추가

    @Autowired
    public ReportController(ReportService reportService, JwtTokenProvider jwtTokenProvider) {
        this.reportService = reportService;
        this.jwtTokenProvider = jwtTokenProvider; // ✅ 생성자 주입
    }

    @PostMapping("/comment")
    public ResponseEntity<ApiResponse<?>> reportComment(@RequestBody ReportCommentRequestDTO request, @RequestHeader("Authorization") String token) {
        Long memberId = jwtTokenProvider.getMemberIdFromToken(token.replace("Bearer ", "")); // ✅ JWT에서 memberId 추출
        reportService.reportComment(request, memberId); // ✅ memberId 전달
        return ResponseEntity.ok(ApiResponse.onSuccess("댓글 신고가 접수되었습니다."));
    }

    @PostMapping("/post")
    public ResponseEntity<ApiResponse<?>> reportPost(@RequestBody ReportPostRequestDTO request, @RequestHeader("Authorization") String token) {
        Long memberId = jwtTokenProvider.getMemberIdFromToken(token.replace("Bearer ", "")); // ✅ JWT에서 memberId 추출
        reportService.reportPost(request, memberId); // ✅ memberId 전달
        return ResponseEntity.ok(ApiResponse.onSuccess("게시글 신고가 접수되었습니다."));
    }
}