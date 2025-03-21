package umc.SukBakJi.domain.Notification.service;


import com.google.firebase.messaging.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import umc.SukBakJi.domain.Notification.model.dto.NotificationRequestDTO;
import umc.SukBakJi.domain.common.entity.enums.NotificationType;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FcmNotificationService {
    private final FirebaseMessaging firebaseMessaging;

    // 단일 기기 알림 전송
    public String sendNotification(NotificationRequestDTO requestDTO) throws FirebaseMessagingException {
        Message message = createMessage(requestDTO);
        String response = firebaseMessaging.send(message);
        return response;
    }

    // 여러 기기 알림 전송
    public BatchResponse sendMulticastNotification(List<String> deviceTokens, NotificationRequestDTO requestDTO) throws FirebaseMessagingException {
        NotificationType type = requestDTO.getType();

        MulticastMessage message = MulticastMessage.builder()
                .addAllTokens(deviceTokens)
                .setNotification(Notification.builder()
                        .setTitle(type.getTitle())
                        .setBody(type.getBody())
                        .build())
                .build();

        BatchResponse response = firebaseMessaging.sendMulticast(message);
        return response;
    }

    // 알림 메시지 생성
    private Message createMessage(NotificationRequestDTO requestDTO) {
        NotificationType type = requestDTO.getType();

        return Message.builder()
                .setToken(requestDTO.getToken())
                .setNotification(Notification.builder()
                        .setTitle(type.getTitle())
                        .setBody(type.getBody())
                        .build())
                .setApnsConfig(ApnsConfig.builder()
                        .setAps(Aps.builder()
                                .setSound("default")
                                .build())
                        .build())
                .build();
    }
}