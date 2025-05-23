package umc.SukBakJi.domain.auth.service;

import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import umc.SukBakJi.domain.auth.converter.AuthConverter;
import umc.SukBakJi.domain.auth.model.dto.AuthRequestDTO;
import umc.SukBakJi.domain.member.converter.MemberConverter;
import umc.SukBakJi.global.apiPayload.exception.GeneralException;
import umc.SukBakJi.global.security.jwt.JwtBlacklistService;
import umc.SukBakJi.global.security.jwt.JwtToken;
import umc.SukBakJi.domain.member.model.dto.MemberRequestDTO;
import umc.SukBakJi.domain.member.model.dto.MemberResponseDTO;
import umc.SukBakJi.domain.member.model.entity.Member;
import umc.SukBakJi.domain.common.entity.enums.Provider;
import umc.SukBakJi.domain.member.repository.MemberRepository;
import umc.SukBakJi.global.apiPayload.code.status.ErrorStatus;
import umc.SukBakJi.global.apiPayload.exception.handler.MemberHandler;
import umc.SukBakJi.global.security.jwt.JwtTokenProvider;
import umc.SukBakJi.global.security.oauth2.service.AppleService;
import umc.SukBakJi.global.security.oauth2.service.KakaoService;

import java.util.Date;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class AuthService {

    private final JwtTokenProvider jwtTokenProvider;
    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final MailService mailService;
    private final KakaoService kakaoService;
    private final AppleService appleService;
    private final JwtBlacklistService jwtBlacklistService;

    // 회원가입
    public void signUp(AuthRequestDTO.SignUpDto requestDto) {
        // 이메일로 회원 조회
        if (memberRepository.findByEmailAndProvider(requestDto.getEmail(), Provider.BASIC).isPresent()) {
            throw new MemberHandler(ErrorStatus.MEMBER_ALREADY_EXISTS);
        }

        String encodedPassword = bCryptPasswordEncoder.encode(requestDto.getPassword());

        Member member = memberRepository.save(
                MemberConverter.toMember(requestDto.getEmail(), encodedPassword, Provider.BASIC, requestDto.getPhoneNumber())
        );
    }

    // 로그인
    public MemberResponseDTO.LoginResponseDto login(AuthRequestDTO.LoginDto loginDto) {
        Member member = memberRepository.findByEmailAndProvider(loginDto.getEmail(), Provider.BASIC)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));

        // 비밀번호 검증
        if (!bCryptPasswordEncoder.matches(loginDto.getPassword(), member.getPassword())) {
            throw new MemberHandler(ErrorStatus.INVALID_PASSWORD);
        }

        JwtToken jwtToken = jwtTokenProvider.generateJwtToken(member);
        return AuthConverter.toLoginDto(Provider.BASIC, member, jwtToken);
    }

    // 이메일 중복 확인
    public Boolean verifyEmail(String email) {
        return !memberRepository.findByEmailAndProvider(email, Provider.BASIC).isPresent();
    }

    public MemberResponseDTO.LoginResponseDto refreshAccessToken(String refreshToken) {
        String token = refreshToken.startsWith("Bearer ") ? refreshToken.substring(7) : refreshToken;

        // refresh token 검증
        if (!jwtTokenProvider.validateToken(token)) {
            throw new GeneralException(ErrorStatus.INVALID_REFRESH_TOKEN);
        }

        // refresh token에서 사용자 아이디 추출
        Long memberId = jwtTokenProvider.getMemberIdFromToken(token);

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));

        JwtToken newJwtToken = jwtTokenProvider.generateJwtToken(member);

        log.info("New Access Token: {}", newJwtToken.getAccessToken());
        log.info("New Refresh Token: {}", newJwtToken.getRefreshToken());

        member.updateRefreshToken(newJwtToken.getRefreshToken());
        memberRepository.save(member);

        return AuthConverter.toLoginDto(Provider.BASIC, member, newJwtToken);
    }

    public String findEmail(MemberRequestDTO.SearchEmailDto requestDto) {
        Optional<Member> member = memberRepository.findByNameAndPhoneNumber(
                requestDto.getName(), requestDto.getPhoneNumber()
        );
        return member.map(m -> maskEmail(m.getEmail()))
                .orElse(ErrorStatus.EMAIL_NOT_FOUND.getCode());
    }

    private String maskEmail(String email) {
        if (email == null || !email.contains("@")) {
            return ErrorStatus.INVALID_EMAIL.getCode();
        }

        String[] parts = email.split("@");
        String localPart = parts[0];
        String domainPart = parts[1];

        int length = localPart.length();
        String visiblePart;
        String maskedPart;

        if (length == 1) {
            visiblePart = "*";
            maskedPart = "";
        } else if (length == 2) {
            visiblePart = localPart.substring(0, 1);
            maskedPart = "*";
        } else if (length == 3) {
            visiblePart = localPart.substring(0, 2);
            maskedPart = "*";
        } else {
            visiblePart = localPart.substring(0, 3);
            maskedPart = "*".repeat(length - 3);
        }

        return visiblePart + maskedPart + "@" + domainPart;
    }

    // 비밀번호 찾기
    public void searchPassword(MemberRequestDTO.SearchPasswordDto request) throws MessagingException {
        mailService.sendMail(request.getEmail());
    }

    // 이메일 인증
    public String verifyEmailCode(MemberRequestDTO.EmailCodeDto request) throws MessagingException {
        boolean isValid = mailService.verifyCode(request.getEmail(), request.getCode());
        if (isValid) {
            mailService.deleteVerificationCode(request.getEmail());
            return "이메일 인증에 성공하였습니다.";
        } else {
            return "인증번호가 일치하지 않거나 만료되었습니다.";
        }
    }

    public void resetPassword(AuthRequestDTO.LoginDto request) {
        Member member = memberRepository.findByEmailAndProvider(request.getEmail(), Provider.BASIC)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));

        // 소셜 로그인 계정은 비밀번호 변경 불가
        if (member.getProvider() != Provider.BASIC) {
            throw new MemberHandler(ErrorStatus.INVALID_EMAIL);
        }
        member.setPassword(bCryptPasswordEncoder.encode(request.getPassword()));
        memberRepository.save(member);
    }

    public MemberResponseDTO.LoginResponseDto oauthLogin(Provider provider, String accessToken) {
        return switch (provider) {
            case KAKAO -> kakaoService.kakaoLogin(accessToken);
            case APPLE -> appleService.appleLogin(accessToken);
            default -> throw new IllegalArgumentException("지원하지 않는 로그인 방식: " + provider);
        };
    }

    public void logOut(String accessToken) {
        if (!jwtTokenProvider.validateToken(accessToken)) {
            throw new MemberHandler(ErrorStatus.INVALID_ACCESS_TOKEN);
        }

        Date expirationDate = jwtTokenProvider.parseClaims(accessToken).getExpiration();
        long expirationMillis = expirationDate.getTime();
        jwtBlacklistService.addToBlacklist(accessToken, expirationMillis);

        Long memberId = jwtTokenProvider.getMemberIdFromToken(accessToken);
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));
        member.resetRefreshToken();
    }
}

