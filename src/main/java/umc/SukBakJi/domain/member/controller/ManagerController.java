package umc.SukBakJi.domain.member.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import umc.SukBakJi.domain.common.entity.enums.UpdateStatus;
import umc.SukBakJi.domain.member.model.dto.EducationVerificationResponseDTO;
import umc.SukBakJi.domain.member.model.entity.Member;
import umc.SukBakJi.domain.member.service.ManagerService;
import umc.SukBakJi.domain.member.service.MemberService;
import umc.SukBakJi.global.apiPayload.ApiResponse;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ManagerController {
    private final ManagerService managerService;

    @GetMapping("/admin/education-certifications")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<EducationVerificationResponseDTO>>> getAllPendingVerifications() {
        return ResponseEntity.ok(ApiResponse.onSuccess(managerService.getAllPendingVerifications()));
    }

    @PostMapping("/admin/education-certifications/{memberId}/approve")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<String>> approve(@PathVariable Long memberId) {
        managerService.approveEducation(memberId);
        return ResponseEntity.ok(ApiResponse.onSuccess("승인이 완료되었습니다."));
    }

    @PostMapping("/admin/education-certifications/{memberId}/reject")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<String>> reject(@PathVariable Long memberId) {
        managerService.rejectEducation(memberId);
        return ResponseEntity.ok(ApiResponse.onSuccess("거절이 완료되었습니다."));
    }
}
