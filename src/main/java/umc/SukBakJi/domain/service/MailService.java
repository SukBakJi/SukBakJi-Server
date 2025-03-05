package umc.SukBakJi.domain.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class MailService {
    private final StringRedisTemplate redisTemplate;
    private final JavaMailSender javaMailSender;
    private static final String senderEmail = "sukbakji240705@gmail.com";

    // 인증 코드 생성
    public String createNumber() {
        Random random = new Random();
        StringBuilder key = new StringBuilder();

        for (int i = 0; i < 8; i++) {
            int index = random.nextInt(3);

            switch (index) {
                case 0 -> key.append((char) (random.nextInt(26) + 97)); // a~z 중 랜덤 문자 key에 추가
                case 1 -> key.append((char) (random.nextInt(26) + 65)); // A~Z 중 랜덤 문자 key에 추가
                case 2 -> key.append(random.nextInt(10)); // 0~9 중 랜덤 숫자 key에 추가
            }
        }
        return key.toString();
    }

    // HTML 이메일 생성
    public MimeMessage createMail(String mail, String number) throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(message, true, "UTF-8");

        messageHelper.setFrom(senderEmail);
        messageHelper.setTo(mail);
        messageHelper.setSubject("[석박지] 이메일 인증번호 발송");

        String body = "<html>";
        body += "<body style='background-color: #000000 !important; margin: 0 auto; max-width: 600px; word-break: break-all; padding-top: 50px; color: #ffffff; text-align: center; font-family: Arial, sans-serif;'>";

        body += "<h1 style='font-size: 28px; font-weight: bold; color: #EC4908;'>이메일 주소 인증</h1>";

        body += "<p style='padding-top: 20px; font-size: 16px; opacity: 0.8; line-height: 28px; font-weight: 400;'>"
                + "안녕하세요? <b>SukBakJi</b> 서비스 관리자입니다.<br>"
                + "비밀번호 재설정을 위해 회원가입 시 입력한 이메일 인증이 필요합니다.<br>"
                + "아래 인증번호를 입력하면 이메일 인증이 완료됩니다.<br>"
                + "감사합니다.</p>";

        body += "<div style='margin-top: 40px; padding: 15px 25px; font-size: 24px; font-weight: bold; color: #ffffff;"
                + "background-color: #EC4908; border-radius: 8px; display: inline-block;'>"
                + number + "</div>";

        body += "<p style='color: #888888; font-size: 12px; margin-top: 40px; opacity: 0.6;'>"
                + "본인이 요청하지 않은 경우, 이 이메일을 무시해 주세요.</p>";

        body += "</body></html>";


        messageHelper.setText(body, true);
        return message;
    }

    // 이메일 전송
    public void sendMail(String email) throws MessagingException {
        String code = createNumber();
        MimeMessage message = createMail(email, code);
        javaMailSender.send(message);
        saveVerificationCode(email, code);
    }

    // 인증 코드 저장
    public void saveVerificationCode(String email, String code) {
        String key = "email_verification:" + email;
        redisTemplate.opsForValue().set(key, code);
        redisTemplate.expire(key, Duration.ofDays(300));
    }

    // 인증 코드 검증
    public boolean verifyCode(String email, String code) {
        String key = "email_verification:" + email;
        String storedCode = redisTemplate.opsForValue().get(key);
        return storedCode != null && storedCode.equals(code);
    }

    // 인증 코드 삭제
    public void deleteVerificationCode(String email) {
        String key = "email_verification:" + email;
        redisTemplate.delete(key);
    }
}