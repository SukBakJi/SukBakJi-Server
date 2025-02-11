package umc.SukBakJi.global.util;

import jakarta.annotation.PostConstruct;
import net.nurigo.sdk.NurigoApp;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.request.SingleMessageSendingRequest;
import net.nurigo.sdk.message.service.DefaultMessageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class CertificationUtil {
    @Value("${coolsms.api.key}")
    private String apiKey;

    @Value("${coolsms.api.secret}")
    private String secretKey;

    @Value("${coolsms.senderPhone}")
    private String senderPhone;

    private DefaultMessageService messageService;

    @PostConstruct
    public void init(){
        this.messageService = NurigoApp.INSTANCE.initialize(apiKey, secretKey, "https://api.coolsms.co.kr");
    }

    public void sendSMS(String userPhone, String certificationCode) {
        Message message = new Message();
        message.setFrom(senderPhone);
        message.setTo(userPhone);
        message.setText("[석박지]\n본인확인 인증번호는 " + certificationCode + "입니다.");
        this.messageService.sendOne(new SingleMessageSendingRequest(message));
    }
}
