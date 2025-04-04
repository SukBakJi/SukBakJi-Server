package umc.SukBakJi.domain.auth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import umc.SukBakJi.domain.auth.model.dto.CertificationDTO;
import umc.SukBakJi.domain.member.model.entity.Member;
import umc.SukBakJi.domain.member.repository.MemberRepository;
import umc.SukBakJi.global.apiPayload.code.status.ErrorStatus;
import umc.SukBakJi.global.apiPayload.exception.handler.MemberHandler;
import umc.SukBakJi.global.util.CertificationUtil;

import java.time.Duration;

@Service
@Transactional
@RequiredArgsConstructor
public class SmsService {
    private final StringRedisTemplate redisTemplate;
    private final CertificationUtil certificationUtil;
    private final MemberRepository memberRepository;

    public void sendVerificationCode(String userPhone) {
        // 인증번호 생성
        String certificationCode = generateVerificationCode();

        // Redis에 인증번호 저장
        saveVerificationCode(userPhone, certificationCode, 5);

        // SMS 전송
        certificationUtil.sendSMS(userPhone, certificationCode);
    }

    private void saveVerificationCode(String userPhone, String code, int expirationMinutes) {
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        valueOperations.set(userPhone, code);
        redisTemplate.expire(userPhone, Duration.ofMinutes(expirationMinutes));
    }

    public void verifyCode(CertificationDTO.smsVerifyDto requestDto) {
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        String storedCode = valueOperations.get(requestDto.getPhoneNumber());

        // 인증번호 불일치
        if (storedCode == null || !storedCode.equals(requestDto.getVerificationCode())) {
            throw new MemberHandler(ErrorStatus.INVALID_CODE);
        }

        // 휴대폰 번호 중복 체크
        if (memberRepository.existsByPhoneNumber(requestDto.getPhoneNumber())) {
            throw new MemberHandler(ErrorStatus.PHONE_ALREADY_EXISTS);
        }
    }

    private String generateVerificationCode() {
        String verificationCode = Integer.toString((int)(Math.random() * (999999 - 100000 + 1)) + 100000);
        return verificationCode;
    }
}