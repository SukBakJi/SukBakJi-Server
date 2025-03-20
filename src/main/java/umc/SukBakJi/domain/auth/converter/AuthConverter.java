package umc.SukBakJi.domain.auth.converter;

import umc.SukBakJi.domain.common.entity.enums.Provider;
import umc.SukBakJi.global.security.jwt.JwtToken;
import umc.SukBakJi.domain.member.model.dto.MemberResponseDto;
import umc.SukBakJi.domain.member.model.entity.Member;

public class AuthConverter {
    public static MemberResponseDto.LoginResponseDto toLoginDto(Provider provider, Member member, JwtToken jwtToken) {
        return MemberResponseDto.LoginResponseDto.builder()
                .provider(provider)
                .email(member.getEmail())
                .accessToken(jwtToken.getAccessToken())
                .refreshToken(jwtToken.getRefreshToken())
                .build();
    }
}
