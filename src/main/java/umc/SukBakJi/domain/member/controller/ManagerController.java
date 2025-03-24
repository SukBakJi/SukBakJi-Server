package umc.SukBakJi.domain.member.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import umc.SukBakJi.domain.common.entity.enums.UpdateStatus;
import umc.SukBakJi.domain.member.model.dto.EducationVerificationResponseDTO;
import umc.SukBakJi.domain.member.model.entity.Member;
import umc.SukBakJi.domain.member.service.ManagerService;
import umc.SukBakJi.domain.member.service.MemberService;
import umc.SukBakJi.global.apiPayload.ApiResponse;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin")
public class ManagerController {
    private final ManagerService managerService;

    @GetMapping("/education-certifications")
    @PreAuthorize("hasRole('ADMIN')")
    public String adminCertList(Model model) {
        List<EducationVerificationResponseDTO> list = managerService.getAllPendingVerifications();
        model.addAttribute("certifications", list);
        return "admin/education-certifications";
    }

    @PostMapping("/education-certifications/{memberId}/approve")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "학력 인증 승인", description = "학력 인증을 승인합니다.")
    public ResponseEntity<ApiResponse<String>> approve(@PathVariable Long memberId) {
        managerService.approveEducation(memberId);
        return ResponseEntity.ok(ApiResponse.onSuccess("승인이 완료되었습니다."));
    }

    @PostMapping("/education-certifications/{memberId}/reject")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "학력 인증 거절", description = "학력 인증을 거절합니다.")
    public ResponseEntity<ApiResponse<String>> reject(@PathVariable Long memberId) {
        managerService.rejectEducation(memberId);
        return ResponseEntity.ok(ApiResponse.onSuccess("거절이 완료되었습니다."));
    }
}
