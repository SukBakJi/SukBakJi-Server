package umc.SukBakJi.domain.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import umc.SukBakJi.domain.converter.AuthConverter;
import umc.SukBakJi.domain.converter.MemberConverter;
import umc.SukBakJi.domain.model.dto.auth.OAuthResponseDTO;
import umc.SukBakJi.domain.model.dto.auth.userInfo.OAuth2UserInfo;
import umc.SukBakJi.global.apiPayload.exception.GeneralException;
import umc.SukBakJi.global.security.jwt.JwtToken;
import umc.SukBakJi.domain.model.dto.member.MemberRequestDto;
import umc.SukBakJi.domain.model.dto.member.MemberResponseDto;
import umc.SukBakJi.domain.model.entity.Member;
import umc.SukBakJi.domain.model.entity.enums.Provider;
import umc.SukBakJi.domain.repository.MemberRepository;
import umc.SukBakJi.global.apiPayload.code.status.ErrorStatus;
import umc.SukBakJi.global.apiPayload.exception.handler.MemberHandler;
import umc.SukBakJi.global.security.jwt.JwtTokenProvider;
import umc.SukBakJi.global.security.oauth2.service.AppleService;
import umc.SukBakJi.global.security.oauth2.service.KakaoService;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class AuthService {

    private final JwtTokenProvider jwtTokenProvider;
    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final KakaoService kakaoService;
    private final AppleService appleService;

    // 회원가입
    public void signUp(MemberRequestDto.SignUpDto requestDto) {
        // 이메일로 회원 조회
        if (memberRepository.findByEmail(requestDto.getEmail()).isPresent()) {
            throw new MemberHandler(ErrorStatus.MEMBER_ALREADY_EXISTS);
        }

        String encodedPassword = bCryptPasswordEncoder.encode(requestDto.getPassword());

        Member member = memberRepository.save(
                MemberConverter.toMember(requestDto.getEmail(), encodedPassword, Provider.BASIC)
        );
    }

    // 로그인
    public MemberResponseDto.LoginResponseDto login(MemberRequestDto.LoginDto loginDto) {
        Member member = memberRepository.findByEmail(loginDto.getEmail())
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));

        // 비밀번호 검증
        if (!bCryptPasswordEncoder.matches(loginDto.getPassword(), member.getPassword())) {
            throw new MemberHandler(ErrorStatus.INVALID_PASSWORD);
        }

        JwtToken jwtToken = jwtTokenProvider.generateJwtToken(member);
        return AuthConverter.toLoginDto(Provider.BASIC, member, jwtToken);
    }

    // 소셜 회원가입 및 로그인
    public Member findOrCreateMember(Provider provider, String email) {
        // 회원이 존재하는지 찾음
        Member member = memberRepository.findByEmailAndProvider(email, provider)
                .orElseGet(() -> {
                    // 회원가입 처리
                    Member newMember = Member.builder()
                            .email(email)
                            .provider(provider)
                            .build();
                    return memberRepository.save(newMember);
                });

        return member;
    }

    // 이메일 중복 확인
    public Boolean verifyEmail(String email) {
        return !memberRepository.findByEmail(email).isPresent();
    }

    public MemberResponseDto.LoginResponseDto refreshAccessToken(String refreshToken) {
        String token = refreshToken.startsWith("Bearer ") ? refreshToken.substring(7) : refreshToken;

        // refresh token 검증
        if (!jwtTokenProvider.validateToken(token)) {
            throw new GeneralException(ErrorStatus.INVALID_REFRESH_TOKEN);
        }

        // refresh token에서 사용자 이메일 추출
        String email = jwtTokenProvider.getEmailFromRefreshToken(token);

        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));

        JwtToken newJwtToken = jwtTokenProvider.generateJwtToken(member);

        log.info("New Access Token: {}", newJwtToken.getAccessToken());
        log.info("New Refresh Token: {}", newJwtToken.getRefreshToken());

        member.updateRefreshToken(newJwtToken.getRefreshToken());
        memberRepository.save(member);

        return AuthConverter.toLoginDto(Provider.BASIC, member, newJwtToken);
    }

    public void logOut(String email) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));
        member.resetRefreshToken();
    }

    public MemberResponseDto.LoginResponseDto oauthLogin(String provider, String accessToken) {
        Provider oauthProvider = Provider.valueOf(provider.toUpperCase());

        return switch (oauthProvider) {
            case KAKAO -> kakaoService.kakaoLogin(accessToken);
//            case APPLE -> appleService.appleLogin(accessToken);
            default -> throw new IllegalArgumentException("지원하지 않는 로그인 방식: " + provider);
        };
    }
}

