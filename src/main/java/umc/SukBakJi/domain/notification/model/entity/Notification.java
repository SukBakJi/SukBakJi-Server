package umc.SukBakJi.domain.notification.model.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import umc.SukBakJi.domain.common.entity.enums.NotificationType;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String message;

    private boolean isRead;

    @CreatedDate
    private LocalDateTime createdTime;

    @Enumerated(EnumType.STRING)
    private NotificationType type;

    public void markAsRead() {
        this.isRead = true;
    }
}
