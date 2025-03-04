package umc.SukBakJi.domain.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import umc.SukBakJi.domain.model.entity.enums.NotificationType;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class NotificationRequestDTO {
    private String token;
    private NotificationType type;
}
