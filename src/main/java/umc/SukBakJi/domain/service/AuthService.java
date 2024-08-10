package umc.SukBakJi.domain.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import umc.SukBakJi.domain.converter.AuthConverter;
import umc.SukBakJi.domain.converter.MemberConverter;
import umc.SukBakJi.domain.model.entity.ResearchTopic;
import umc.SukBakJi.domain.model.entity.mapping.MemberResearchTopic;
import umc.SukBakJi.domain.repository.MemberResearchTopicRepository;
import umc.SukBakJi.domain.repository.ResearchTopicRepository;
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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {

    private final JwtTokenProvider jwtTokenProvider;
    private final MemberRepository memberRepository;
    private final ResearchTopicRepository researchTopicRepository;
    private final MemberResearchTopicRepository memberResearchTopicRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
//    private final KakaoService kakaoService;
//    private final AppleService appleService;

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
        return AuthConverter.toLoginDto(member, jwtToken);
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

    public void logOut(String email) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));
        member.resetRefreshToken();
    }

//    public JwtToken generateJwtToken(Member member) {
//        return jwtTokenProvider.generateToken(new UsernamePasswordAuthenticationToken(
//                member.getEmail(), member.getPassword()
//        ));
//    }

//    private JwtToken getToken(Member member) {
//        JwtToken token = generateJwtToken(member);
//        member.updateRefreshToken(token.getRefreshToken());
//        return token;
//    }
//
//
//
//    public String getAccessToken(String providerId, String code) {
//        return switch (Provider.valueOf(providerId.toUpperCase())) {
//            case KAKAO -> kakaoService.getAccessToken(code);
//            case APPLE -> appleService.getAccessToken(code);
//            default -> throw new IllegalArgumentException("지원하지 않는 로그인 방식: " + providerId);
//        };
//    }
//
//    public Map<String, Object> getUserInfo(String providerId, String accessToken) {
//        return switch (Provider.valueOf(providerId.toUpperCase())) {
//            case KAKAO -> kakaoService.getKakaoUserInfo(accessToken);
//            case APPLE -> appleService.getAppleUserInfo(accessToken);
//            default -> throw new IllegalArgumentException("지원하지 않는 로그인 방식: " + providerId);
//        };
//    }
//
//    private Member saveMember(Provider provider, String email) {
//        Member member = Member.builder()
//                .email(email)
//                .provider(provider)
//                .build();
//        return memberRepository.save(member);
//    }
//
//    public Member getMemberFromToken(String token) {
//        // JWT 토큰 검증
//        if (!jwtTokenProvider.validateToken(token)) {
//            throw new IllegalArgumentException("Invalid Token");
//        }
//
//        // JWT 토큰에서 사용자 이메일 추출
//        String email = jwtTokenProvider.getEmailFromToken(token);
//
//        // 이메일로 사용자 조회
//        return memberRepository.findByEmail(email)
//                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));
//    }
}

