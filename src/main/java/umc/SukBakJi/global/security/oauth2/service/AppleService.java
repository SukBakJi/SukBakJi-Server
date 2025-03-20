package umc.SukBakJi.global.security.oauth2.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

import java.util.Optional;

@Service
@Slf4j
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

    public MemberResponseDto.LoginResponseDto appleLogin(String authorizationCode) {
        AppleIdTokenPayload appleUser;
        try {
            appleUser = getAppleUser(authorizationCode);
            if (appleUser == null) {
                throw new RuntimeException("Apple ID Token 디코딩 실패");
            }
            log.info("애플 로그인 성공: {}", appleUser.getEmail());
        } catch (Exception e) {
            throw new RuntimeException("Apple ID Token 디코딩 실패", e);
        }

        try {
            // 회원 가입 또는 로그인 처리
            Member member = saveOrUpdate(new AppleUserInfo(appleUser.getEmail()));

            // JWT 토큰 생성
            JwtToken jwtToken = jwtTokenProvider.generateJwtToken(member);

            return AuthConverter.toLoginDto(Provider.APPLE, member, jwtToken);
        } catch (Exception e) {
            log.error("애플 로그인 실패: {}", e.getMessage(), e);
            throw new RuntimeException("애플 로그인 실패", e);
        }
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
        log.info("애플 토큰 응답: {}", response);

        AppleIdTokenPayload payload = appleJwtUtil.decodeJwt(response.getIdToken());
        log.info("Apple ID Token Payload: {}", payload);

        Optional<Member> existingMember = memberRepository.findBySubAndProvider(payload.getSub(), Provider.APPLE);
        String email = payload.getEmail();
        if (email == null && existingMember.isPresent()) {
            email = existingMember.get().getEmail();
        }

        return AppleIdTokenPayload.builder()
                .sub(payload.getSub())
                .email(email)
                .build();
    }

    // 사용자 정보 저장 및 업데이트
    private Member saveOrUpdate(AppleUserInfo appleUserInfo) {
        // sub으로 회원 조회
        Optional<Member> existingMember = memberRepository.findBySubAndProvider(appleUserInfo.getSub(), Provider.APPLE);

        if (existingMember.isPresent()) {
            return existingMember.get();
        }

        // sub이 없으면 email로 조회
        Optional<Member> memberByEmail = memberRepository.findByEmailAndProvider(appleUserInfo.getEmail(), Provider.APPLE);

        if (memberByEmail.isPresent()) {
            // 회원이 존재하지만 sub이 없을 경우, sub 업데이트
            Member member = memberByEmail.get();
            if (member.getSub() == null) {
                member.setSub(appleUserInfo.getSub());
                memberRepository.save(member);
            }
            return member;
        }

        return memberRepository.save(Member.builder()
                .sub(appleUserInfo.getSub())
                .email(appleUserInfo.getEmail() == null ? appleUserInfo.getSub() : appleUserInfo.getEmail())
                .provider(Provider.APPLE)
                .build());
    }
}