package umc.SukBakJi.global.security.oauth2.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import umc.SukBakJi.domain.auth.converter.AuthConverter;
import umc.SukBakJi.domain.auth.model.dto.AppleIdTokenPayload;
import umc.SukBakJi.domain.auth.model.dto.AppleTokenResponse;
import umc.SukBakJi.domain.auth.model.dto.AppleUserInfo;
import umc.SukBakJi.domain.member.model.dto.MemberResponseDto;
import umc.SukBakJi.domain.member.model.entity.Member;
import umc.SukBakJi.domain.common.entity.enums.Provider;
import umc.SukBakJi.domain.member.repository.MemberRepository;
import umc.SukBakJi.global.security.jwt.JwtToken;
import umc.SukBakJi.global.security.jwt.JwtTokenProvider;
import umc.SukBakJi.global.util.AppleJwtUtil;

@Service
@RequiredArgsConstructor
public class AppleService {
    @Value("${apple.team-id}")
    private String APPLE_TEAM_ID;

    @Value("${apple.private-key-path}")
    private String APPLE_PRIVATE_KEY;

    @Value("${spring.security.oauth2.client.registration.apple.client-id}")
    private String APPLE_CLIENT_ID;

    @Value("${spring.security.oauth2.client.registration.apple.authorization-grant-type}")
    private String APPLE_GRANT_TYPE;

    @Value("${spring.security.oauth2.client.registration.apple.redirect-uri}")
    private String APPLE_REDIRECT_URL;

    @Value("${apple.key-id}")
    private String APPLE_KEY_ID;

    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final AppleAuthClient appleAuthClient;
    private final AppleJwtUtil appleJwtUtil;

    public MemberResponseDto.LoginResponseDto appleLogin(String authorizationCode) throws Exception {
        AppleIdTokenPayload appleUser = getAppleUser(authorizationCode);

        // 회원 가입 또는 로그인 처리
        Member member = saveOrUpdate(new AppleUserInfo(appleUser.getEmail()));
        JwtToken jwtToken = jwtTokenProvider.generateJwtToken(member);

        return AuthConverter.toLoginDto(Provider.APPLE, member, jwtToken);
    }

    // 애플 사용자 정보 가져오기
    public AppleIdTokenPayload getAppleUser(String authorizationCode) throws Exception {
        String clientSecret = AppleJwtUtil.generateClientSecret(
                APPLE_TEAM_ID,
                APPLE_KEY_ID,
                APPLE_CLIENT_ID,
                APPLE_PRIVATE_KEY
        );

        AppleTokenResponse response = appleAuthClient.getIdToken(
                APPLE_CLIENT_ID,
                clientSecret,
                APPLE_GRANT_TYPE,
                authorizationCode
        );

        return appleJwtUtil.decodeJwt(response.getIdToken());
    }

    // 사용자 정보 저장 및 업데이트
    private Member saveOrUpdate(AppleUserInfo appleUserInfo) {
        return memberRepository.findByEmailAndProvider(appleUserInfo.getEmail(), Provider.APPLE)
                .orElseGet(() -> memberRepository.save(Member.builder()
                        .email(appleUserInfo.getEmail())
                        .provider(Provider.APPLE)
                        .build()));
    }
}