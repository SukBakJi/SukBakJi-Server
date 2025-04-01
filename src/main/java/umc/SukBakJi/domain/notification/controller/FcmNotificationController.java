package umc.SukBakJi.domain.notification.controller;

import com.google.firebase.messaging.FirebaseMessagingException;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import umc.SukBakJi.domain.notification.model.dto.NotificationRequestDTO;
import umc.SukBakJi.domain.notification.service.FcmNotificationService;
import umc.SukBakJi.global.apiPayload.ApiResponse;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/notification")
public class FcmNotificationController {
    private final FcmNotificationService fcmNotificationService;
    @PostMapping
    @Operation(summary = "알림 전송", description = "알림 유형에 따라 알림을 보냅니다.")
    public ApiResponse<String> sendNotification(@RequestBody @Valid NotificationRequestDTO requestDto) throws FirebaseMessagingException {
        fcmNotificationService.sendNotification(requestDto);
        return ApiResponse.onSuccess("알림 전송 성공");
    }

    @PostMapping("/multicast")
    @Operation(summary = "여러 기기에게 알림 전송", description = "FCM을 이용해 여러 기기에 알림을 보냅니다.")
    public ApiResponse<String> sendMulticastNotification(@RequestBody List<String> deviceTokens, @RequestBody @Valid NotificationRequestDTO requestDto) throws FirebaseMessagingException {
        fcmNotificationService.sendMulticastNotification(deviceTokens, requestDto);
        return ApiResponse.onSuccess("멀티캐스트 알림 전송 성공");
    }
}