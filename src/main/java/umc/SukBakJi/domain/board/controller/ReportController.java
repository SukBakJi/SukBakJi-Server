package umc.SukBakJi.domain.board.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import umc.SukBakJi.domain.board.model.dto.ReportCommentRequestDTO;
import umc.SukBakJi.domain.board.model.dto.ReportPostRequestDTO;
import umc.SukBakJi.domain.board.service.ReportService;
import umc.SukBakJi.global.apiPayload.ApiResponse;
import umc.SukBakJi.global.security.PrincipalDetails;
import umc.SukBakJi.global.security.jwt.JwtTokenProvider;

@Tag(name = "신고 API", description = "게시글 및 댓글 신고 기능을 제공합니다.")
@RestController
@RequestMapping("/api/reports")
public class ReportController {

    private final ReportService reportService;

    @Autowired
    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @Operation(summary = "댓글 신고", description = "특정 댓글을 신고합니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "댓글 신고 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "유효하지 않은 요청"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @PostMapping("/comment")
    public ResponseEntity<ApiResponse<?>> reportComment(@RequestBody ReportCommentRequestDTO request, @AuthenticationPrincipal PrincipalDetails principalDetails) {
        Long memberId = principalDetails.getMember().getMemberId();
        reportService.reportComment(request, memberId); // ✅ memberId 전달
        return ResponseEntity.ok(ApiResponse.onSuccess("댓글 신고가 접수되었습니다."));
    }

    @Operation(summary = "게시글 신고", description = "특정 게시글을 신고합니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "게시글 신고 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "유효하지 않은 요청"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @PostMapping("/post")
    public ResponseEntity<ApiResponse<?>> reportPost(@RequestBody ReportPostRequestDTO request, @AuthenticationPrincipal PrincipalDetails principalDetails
    ) {
        Long memberId = principalDetails.getMember().getMemberId();

        reportService.reportPost(request, memberId); // ✅ memberId 전달
        return ResponseEntity.ok(ApiResponse.onSuccess("게시글 신고가 접수되었습니다."));
    }
}