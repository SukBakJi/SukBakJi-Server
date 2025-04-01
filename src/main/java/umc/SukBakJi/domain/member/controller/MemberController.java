package umc.SukBakJi.domain.member.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import umc.SukBakJi.domain.member.model.dto.MemberRequestDTO;
import umc.SukBakJi.domain.member.model.dto.MemberResponseDTO;
import umc.SukBakJi.domain.member.service.ManagerService;
import umc.SukBakJi.domain.member.service.MemberService;
import umc.SukBakJi.global.apiPayload.ApiResponse;
import umc.SukBakJi.global.security.PrincipalDetails;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/api/user")
public class MemberController {
    private final MemberService memberService;
    private final ManagerService managerService;

    @PostMapping("/profile")
    @Operation(summary = "프로필 설정", description = "회원가입 이후 진행되는 프로필 설정입니다.")
    public ApiResponse<MemberResponseDTO.ProfileResultDto> setMemberProfile(@RequestHeader("Authorization") String token,
                                                                            @RequestBody MemberRequestDTO.ProfileDto profileDto) {
        String jwtToken = token.substring(7);
        MemberResponseDTO.ProfileResultDto responseDto = memberService.setMemberProfile(jwtToken, profileDto);
        return ApiResponse.onSuccess(responseDto);
    }

    @PutMapping("/profile")
    @Operation(summary = "프로필 수정", description = "설정에서 사용자 정보를 수정합니다.")
    public ApiResponse<MemberResponseDTO.ProfileResultDto> modifyMemberProfile(@RequestHeader("Authorization") String token,
                                                                               @RequestBody MemberRequestDTO.ModifyProfileDto profileDto) {
        String jwtToken = token.substring(7);
        MemberResponseDTO.ProfileResultDto responseDto = memberService.modifyMemberProfile(jwtToken, profileDto);
        return ApiResponse.onSuccess(responseDto);
    }

    @PostMapping(value = "/education-certification", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE})
    @Operation(summary = "학력 인증 이미지 첨부", description = "재학증명서, 학생증 또는 졸업증명서에 해당하는 사진을 첨부하여 학력 인증을 진행합니다. 첨부한 사진은 클라우드 저장소에 저장됩니다.")
    public ApiResponse<String> uploadEducationCertificateImage(@RequestHeader("Authorization") String token,
                                                               @RequestPart("certificationPicture") MultipartFile certificationPicture,
                                                               @RequestPart(value = "educationCertificateType") String educationCertificateType) {
        String jwtToken = token.substring(7);
        memberService.uploadEducationCertificate(jwtToken, certificationPicture, educationCertificateType);
        return ApiResponse.onSuccess("학력 인증을 위한 이미지 전송에 성공하였습니다.");
    }

    @GetMapping("/mypage")
    @Operation(summary = "마이페이지", description = "사용자가 설정한 프로필 정보를 확인합니다.")
    public ApiResponse<MemberResponseDTO.ProfileResultDto> getMyPage(@RequestHeader("Authorization") String token) {
        String jwtToken = token.substring(7);
        MemberResponseDTO.ProfileResultDto responseDto = memberService.getMemberProfile(jwtToken);
        return ApiResponse.onSuccess(responseDto);
    }

    @PostMapping("/password-reset")
    @Operation(summary = "비밀번호 재설정", description = "비밀번호를 재설정합니다.")
    public ApiResponse<String> resetPassword(@AuthenticationPrincipal PrincipalDetails principalDetails,
                                        @Valid @RequestBody MemberRequestDTO.ModifyPasswordDto modifyPasswordDto) {
        Long memberId = principalDetails.getMember().getMemberId();
        memberService.resetPassword(memberId, modifyPasswordDto);
        return ApiResponse.onSuccess("비밀번호를 재설정하였습니다.");
    }

    @PostMapping("/apple-email")
    @Operation(summary = "애플 이메일 설정", description = "애플 이메일을 설정합니다.")
    public ApiResponse<String> setAppleEmail(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @Valid @RequestBody MemberRequestDTO.AppleDto request) {
        Long memberId = principalDetails.getMember().getMemberId();
        memberService.setAppleEmail(memberId, request);
        return ApiResponse.onSuccess("이메일을 설정하였습니다.");
    }

    @PostMapping("/fcm-token")
    @Operation(summary = "FCM 기기 토큰 설정", description = "FCM 기기 토큰을 설정하였습니다.")
    public ApiResponse<String> setDeviceToken(
            @AuthenticationPrincipal PrincipalDetails principalDetails,
            @Valid @RequestBody MemberRequestDTO.DeviceTokenDto request) {
        Long memberId = principalDetails.getMember().getMemberId();
        memberService.setDeviceToken(memberId, request);
        return ApiResponse.onSuccess("FCM 기기 토큰을 설정하였습니다.");
    }
}