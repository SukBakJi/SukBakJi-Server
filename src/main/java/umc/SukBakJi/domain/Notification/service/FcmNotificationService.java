package umc.SukBakJi.domain.Notification.service;


import com.google.firebase.messaging.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import umc.SukBakJi.domain.Notification.model.dto.NotificationRequestDTO;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class FcmNotificationService {
    private final FirebaseMessaging firebaseMessaging;

    // 단일 기기 알림 전송
    public String sendNotification(NotificationRequestDTO requestDTO) throws FirebaseMessagingException {
        Message message = createMessage(requestDTO);
        String response = firebaseMessaging.send(message);
        log.info("FCM 알림 전송 성공: " + response);
        return response;
    }

    // 여러 기기 알림 전송
    public BatchResponse sendMulticastNotification(List<String> deviceTokens, NotificationRequestDTO requestDTO) throws FirebaseMessagingException {
        MulticastMessage message = MulticastMessage.builder()
                .addAllTokens(deviceTokens)
                .setNotification(Notification.builder()
                        .setTitle(requestDTO.getTitle())
                        .setBody(requestDTO.getBody())
                        .build())
                .build();

        BatchResponse response = firebaseMessaging.sendMulticast(message);
        log.info("FCM 알림 전송 성공: " + response);
        return response;
    }

    // 알림 메시지 생성
    private Message createMessage(NotificationRequestDTO requestDTO) {
        return Message.builder()
                .setToken(requestDTO.getDeviceToken())
                .setNotification(Notification.builder()
                        .setTitle(requestDTO.getTitle())
                        .setBody(requestDTO.getBody())
                        .build())
                .setApnsConfig(ApnsConfig.builder()
                        .setAps(Aps.builder()
                                .setSound("default")
                                .build())
                        .build())
                .build();
    }
}