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
        return member.map(Member::getEmail).orElse("EMAIL_NOT_FOUND");  // 이메일이 존재하지 않음
    }

    private String generateVerificationCode() {
        String verificationCode = Integer.toString((int)(Math.random() * (999999 - 100000 + 1)) + 100000);
        return verificationCode;
    }
}
