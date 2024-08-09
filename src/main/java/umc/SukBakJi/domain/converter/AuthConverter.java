package umc.SukBakJi.domain.converter;

import umc.SukBakJi.global.security.jwt.JwtToken;
import umc.SukBakJi.domain.model.dto.member.MemberResponseDto;
import umc.SukBakJi.domain.model.entity.Member;

public class AuthConverter {
    public static MemberResponseDto.LoginResponseDto toLoginDto(Member member, JwtToken jwtToken) {
        return MemberResponseDto.LoginResponseDto.builder()
                .email(member.getEmail())
                .accessToken(jwtToken.getAccessToken())
                .refreshToken(jwtToken.getRefreshToken())
                .build();
    }
}
