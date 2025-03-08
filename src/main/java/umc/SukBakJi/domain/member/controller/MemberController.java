package umc.SukBakJi.domain.member.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import umc.SukBakJi.domain.member.model.dto.MemberRequestDto;
import umc.SukBakJi.domain.member.model.dto.MemberResponseDto;
import umc.SukBakJi.domain.member.service.MemberService;
import umc.SukBakJi.global.apiPayload.ApiResponse;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/api/user")
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/profile")
    @Operation(summary = "프로필 설정", description = "회원가입 이후 진행되는 프로필 설정입니다.")
    public ApiResponse<MemberResponseDto.ProfileResultDto> setMemberProfile(@RequestHeader("Authorization") String token,
                                                                            @RequestBody MemberRequestDto.ProfileDto profileDto) {
        String jwtToken = token.substring(7);
        MemberResponseDto.ProfileResultDto responseDto = memberService.setMemberProfile(jwtToken, profileDto);
        return ApiResponse.onSuccess(responseDto);
    }

    @PutMapping("/profile")
    @Operation(summary = "프로필 수정", description = "설정에서 사용자 정보를 수정합니다.")
    public ApiResponse<MemberResponseDto.ProfileResultDto> modifyMemberProfile(@RequestHeader("Authorization") String token,
                                                                               @RequestBody MemberRequestDto.ModifyProfileDto profileDto) {
        String jwtToken = token.substring(7);
        MemberResponseDto.ProfileResultDto responseDto = memberService.modifyMemberProfile(jwtToken, profileDto);
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
    public ApiResponse<MemberResponseDto.ProfileResultDto> getMyPage(@RequestHeader("Authorization") String token) {
        String jwtToken = token.substring(7);
        MemberResponseDto.ProfileResultDto responseDto = memberService.getMemberProfile(jwtToken);
        return ApiResponse.onSuccess(responseDto);
    }

    @PostMapping("/email")
    @Operation(summary = "이름과 전화번호로 이메일 찾기", description = "이름과 전화번호로 등록된 이메일을 일부 반환합니다.")
    public ResponseEntity<ApiResponse<String>> findEmail(@RequestBody MemberRequestDto.searchEmailDto requestDto) {
        String response = memberService.findEmail(requestDto);
        return ResponseEntity.ok(ApiResponse.onSuccess(response));
    }

    @PostMapping("/password")
    @Operation(summary = "비밀번호 찾기", description = "이메일을 입력하여 해당 이메일로 인증번호를 전송합니다.")
    public ApiResponse<String> findPassword(@AuthenticationPrincipal Long memberId,
                                             @Valid @RequestBody MemberRequestDto.SearchPasswordDto searchPasswordDto) throws MessagingException {
        memberService.searchPassword(memberId, searchPasswordDto);
        return ApiResponse.onSuccess("비밀번호 재설정에 필요한 인증번호가 이메일로 전송되었습니다.");
    }

    @PostMapping("/email-code")
    @Operation(summary = "이메일 인증번호 인증", description = "이메일로 전달된 인증번호를 검사합니다.")
    public ResponseEntity<ApiResponse<String>> verifyEmailCode(@AuthenticationPrincipal Long memberId,
                                            @Valid @RequestBody MemberRequestDto.EmailCodeDto emailCodeDto) throws MessagingException {
        String response = memberService.verifyEmailCode(memberId, emailCodeDto);
        return ResponseEntity.ok(ApiResponse.onSuccess(response));
    }

    @PostMapping("/password-reset")
    @Operation(summary = "비밀번호 재설정", description = "비밀번호를 재설정합니다.")
    public ApiResponse<String> resetPassword(@AuthenticationPrincipal Long memberId,
                                        @Valid @RequestBody MemberRequestDto.ModifyPasswordDto modifyPasswordDto) {
        memberService.resetPassword(memberId, modifyPasswordDto);
        return ApiResponse.onSuccess("비밀번호를 재설정하였습니다.");
    }
}