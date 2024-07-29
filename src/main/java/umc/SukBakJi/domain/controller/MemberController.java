package umc.SukBakJi.domain.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import umc.SukBakJi.domain.model.dto.member.MemberRequestDto;
import umc.SukBakJi.domain.model.dto.member.MemberResponseDto;
import umc.SukBakJi.domain.service.MemberService;
import umc.SukBakJi.global.apiPayload.ApiResponse;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/api/user")
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/profile")
    @Operation(summary = "프로필 설정", description = "회원가입 이후 진행되는 프로필 설정입니다.")
    public ApiResponse<?> setMemberProfile(@RequestBody MemberRequestDto.ProfileDto profileDto) {
        MemberResponseDto.ProfileResultDto responseDto = memberService.setMemberProfile(profileDto);
        return ApiResponse.onSuccess(responseDto);
    }
}