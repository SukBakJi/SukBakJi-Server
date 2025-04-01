package umc.SukBakJi.domain.notification.aop;

import com.google.firebase.messaging.FirebaseMessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import umc.SukBakJi.domain.notification.model.dto.NotificationRequestDTO;
import umc.SukBakJi.domain.notification.service.FcmNotificationService;
import umc.SukBakJi.domain.board.model.entity.Comment;
import umc.SukBakJi.domain.common.entity.enums.NotificationType;
import umc.SukBakJi.domain.member.model.entity.Member;

@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class NotificationAspect {

    private final FcmNotificationService fcmNotificationService;

    /**
     * 댓글 작성 후 알림 전송
     */
    @AfterReturning(value = "execution(* umc.SukBakJi.domain.board.service.CommentService.createComment(..))", returning = "comment")
    public void sendNotificationAfterComment(JoinPoint joinPoint, Comment comment) {
        try {
            NotificationRequestDTO notification = NotificationRequestDTO
                    .builder()
                    .deviceToken(comment.getPost().getMember().getFcmToken())
                    .title(NotificationType.EDUCATION_CERTIFICATION.getTitle())
                    .body(NotificationType.EDUCATION_CERTIFICATION.getBody())
                    .build();
            fcmNotificationService.sendNotification(notification);
            log.info("댓글 알림 전송 완료");
        } catch (FirebaseMessagingException e) {
            log.error("댓글 알림 전송 실패", e);
        }
    }

    /**
     * 학력 인증 완료 후 알림 전송
     */
    @AfterReturning(
            value = "execution(* umc.SukBakJi.domain.member.service.ManagerService.approveEducation(..))",
            returning = "member"
    )
    public void sendNotificationAfterVerification(JoinPoint joinPoint, Member member) {
        if (member == null || member.getFcmToken() == null) {
            log.warn("FCM 토큰이 없거나 회원 정보가 없습니다.");
            return;
        }
        sendNotification(member, NotificationType.EDUCATION_CERTIFICATION);
    }

    @AfterReturning(
            value = "execution(* umc.SukBakJi.domain.member.service.ManagerService.rejectEducation(..))",
            returning = "member"
    )
    public void sendNotificationAfterRejection(JoinPoint joinPoint, Member member) {
        if (member == null || member.getFcmToken() == null) {
            log.warn("FCM 토큰이 없거나 회원 정보가 없습니다.");
            return;
        }

        sendNotification(member, NotificationType.FAILED_EDUCATION_CERTIFICATION);
    }

    private void sendNotification(Member member, NotificationType type) {
        if (member.getFcmToken() == null) {
            log.warn("FCM 토큰이 없어 알림을 전송하지 않습니다.");
            return;
        }

        try {
            NotificationRequestDTO notification = NotificationRequestDTO.builder()
                    .deviceToken(member.getFcmToken())
                    .title(type.getTitle())
                    .body(type.getBody())
                    .build();
            fcmNotificationService.sendNotification(notification);
            log.info("학력 인증 알림 전송 완료: {}", member.getEmail());
        } catch (FirebaseMessagingException e) {
            log.error("학력 인증 알림 전송 실패", e);
        }
    }
}
