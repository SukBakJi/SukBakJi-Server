package umc.SukBakJi.domain.auth.service;

import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import umc.SukBakJi.domain.auth.converter.AuthConverter;
import umc.SukBakJi.domain.member.converter.MemberConverter;
import umc.SukBakJi.global.apiPayload.exception.GeneralException;
import umc.SukBakJi.global.security.jwt.JwtToken;
import umc.SukBakJi.domain.member.model.dto.MemberRequestDto;
import umc.SukBakJi.domain.member.model.dto.MemberResponseDto;
import umc.SukBakJi.domain.member.model.entity.Member;
import umc.SukBakJi.domain.common.entity.enums.Provider;
import umc.SukBakJi.domain.member.repository.MemberRepository;
import umc.SukBakJi.global.apiPayload.code.status.ErrorStatus;
import umc.SukBakJi.global.apiPayload.exception.handler.MemberHandler;
import umc.SukBakJi.global.security.jwt.JwtTokenProvider;
import umc.SukBakJi.global.security.oauth2.service.AppleService;
import umc.SukBakJi.global.security.oauth2.service.KakaoService;

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

    public String findEmail(MemberRequestDto.searchEmailDto requestDto) {
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
    public void searchPassword(MemberRequestDto.SearchPasswordDto request) throws MessagingException {
        mailService.sendMail(request.getEmail());
    }

    // 이메일 인증
    public String verifyEmailCode(MemberRequestDto.EmailCodeDto request) throws MessagingException {
        boolean isValid = mailService.verifyCode(request.getEmail(), request.getCode());
        if (isValid) {
            mailService.deleteVerificationCode(request.getEmail());
            return "이메일 인증에 성공하였습니다.";
        } else {
            return "인증번호가 일치하지 않거나 만료되었습니다.";
        }
    }

    public void logOut(String email) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));
        member.resetRefreshToken();
    }

    public MemberResponseDto.LoginResponseDto oauthLogin(Provider provider, String accessToken) {
        return switch (provider) {
            case KAKAO -> kakaoService.kakaoLogin(accessToken);
//            case APPLE -> appleService.appleLogin(accessToken);
            default -> throw new IllegalArgumentException("지원하지 않는 로그인 방식: " + provider);
        };
    }
}

