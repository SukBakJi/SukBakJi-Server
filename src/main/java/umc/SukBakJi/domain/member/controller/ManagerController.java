package umc.SukBakJi.domain.member.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import umc.SukBakJi.domain.member.model.dto.EducationVerificationResponseDTO;
import umc.SukBakJi.domain.member.service.ManagerService;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class ManagerController {
    private final ManagerService managerService;

    @GetMapping("/login")
    public String loginPage() {
        return "admin/login";
    }

    @GetMapping("/education-certifications")
    @PreAuthorize("hasRole('ADMIN')")
    public String showEducationCertifications(Model model) {
        List<EducationVerificationResponseDTO> certifications = managerService.getAllPendingVerifications();
        model.addAttribute("certifications", certifications);
        return "admin/education-certifications";
    }

    @PostMapping("/education-certifications/{memberId}/approve")
    @PreAuthorize("hasRole('ADMIN')")
    public String approve(@PathVariable Long memberId) {
        managerService.approveEducation(memberId);
        return "redirect:/admin/education-certifications";
    }

    @PostMapping("/education-certifications/{memberId}/reject")
    @PreAuthorize("hasRole('ADMIN')")
    public String reject(@PathVariable Long memberId) {
        managerService.rejectEducation(memberId);
        return "redirect:/admin/education-certifications";
    }

}
