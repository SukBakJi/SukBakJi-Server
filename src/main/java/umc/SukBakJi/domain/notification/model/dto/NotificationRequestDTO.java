package umc.SukBakJi.domain.notification.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class NotificationRequestDTO {
    private String deviceToken;
    private String title;
    private String body;
}