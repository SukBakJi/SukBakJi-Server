package umc.SukBakJi.global.security.oauth2.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import umc.SukBakJi.domain.auth.converter.AuthConverter;
import umc.SukBakJi.domain.auth.model.dto.AppleUserInfo;
import umc.SukBakJi.domain.member.model.dto.MemberResponseDto;
import umc.SukBakJi.domain.member.model.entity.Member;
import umc.SukBakJi.domain.common.entity.enums.Provider;
import umc.SukBakJi.domain.member.repository.MemberRepository;
import umc.SukBakJi.global.security.jwt.JwtToken;
import umc.SukBakJi.global.security.jwt.JwtTokenProvider;

import java.security.PublicKey;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AppleService {
    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final AppleTokenValidator appleTokenValidator;

    public MemberResponseDto.LoginResponseDto appleLogin(String idToken) {
        AppleUserInfo userInfo = validateAppleToken(idToken);

        // 회원 가입 또는 로그인 처리
        Member member = saveOrUpdate(userInfo);
        JwtToken jwtToken = jwtTokenProvider.generateJwtToken(member);

        return AuthConverter.toLoginDto(Provider.KAKAO, member, jwtToken);
    }

    // 애플 ID 토큰 검증
    private AppleUserInfo validateAppleToken(String idToken) {
        try {
            PublicKey applePublicKey = appleTokenValidator.getApplePublicKey(idToken);
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(applePublicKey)
                    .build()
                    .parseClaimsJws(idToken)
                    .getBody();

            String email = claims.get("email", String.class);

            return new AppleUserInfo(Map.of("email", email));
        } catch (Exception e) {
            throw new IllegalStateException("애플 ID 토큰 검증 실패", e);
        }
    }

    // 사용자 정보 저장 및 업데이트
    private Member saveOrUpdate(AppleUserInfo appleUserInfo) {
        return memberRepository.findByEmailAndProvider(appleUserInfo.getEmail(), appleUserInfo.getProvider())
                .orElseGet(() -> memberRepository.save(Member.builder()
                        .email(appleUserInfo.getEmail())
                        .provider(appleUserInfo.getProvider())
                        .build()));
    }
}
