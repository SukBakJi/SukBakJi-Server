package umc.SukBakJi.domain.converter;

import umc.SukBakJi.domain.model.entity.enums.Provider;
import umc.SukBakJi.global.security.jwt.JwtToken;
import umc.SukBakJi.domain.model.dto.member.MemberResponseDto;
import umc.SukBakJi.domain.model.entity.Member;

public class AuthConverter {
    public static MemberResponseDto.LoginResponseDto toLoginDto(Provider provider, Member member, JwtToken jwtToken) {
        return MemberResponseDto.LoginResponseDto.builder()
                .provider(provider.getValue())
                .email(member.getEmail())
                .accessToken(jwtToken.getAccessToken())
                .refreshToken(jwtToken.getRefreshToken())
                .build();
    }
}
