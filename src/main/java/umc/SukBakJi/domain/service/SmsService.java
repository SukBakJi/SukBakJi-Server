package umc.SukBakJi.domain.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import umc.SukBakJi.domain.model.dto.CertificationDTO;
import umc.SukBakJi.domain.model.entity.Member;
import umc.SukBakJi.domain.repository.MemberRepository;
import umc.SukBakJi.global.util.CertificationUtil;

import java.time.Duration;
import java.util.Optional;

@Service
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
        redisTemplate.expire(userPhone, Duration.ofSeconds(expirationMinutes * 60L));
    }

    public String verifyAndFindEmail(CertificationDTO.smsVerifyDto requestDto) {
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        String storedCode = valueOperations.get(requestDto.getPhoneNumber());

        // 인증번호 불일치
        if (storedCode == null || !storedCode.equals(requestDto.getVerificationCode())) {
            return "INVALID_CODE";  // 인증 실패
        }

        // 인증 성공 후 이메일 조회
        Optional<Member> member = memberRepository.findByPhoneNumber(requestDto.getPhoneNumber());
        return member.map(m -> maskEmail(m.getEmail()))
                .orElse("EMAIL_NOT_FOUND");  // 이메일이 존재하지 않음
    }

    private String maskEmail(String email) {
        if (email == null || !email.contains("@")) {
            return "INVALID_EMAIL";
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

    private String generateVerificationCode() {
        String verificationCode = Integer.toString((int)(Math.random() * (999999 - 100000 + 1)) + 100000);
        return verificationCode;
    }
}
