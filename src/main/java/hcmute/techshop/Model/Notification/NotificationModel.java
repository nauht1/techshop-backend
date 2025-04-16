package hcmute.techshop.Model.Notification;

import hcmute.techshop.Enum.NotificationType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NotificationModel {
    private Integer id;
    private Integer userId;
    private String title;
    private String message;
    private boolean isRead;
    private NotificationType type;
    private LocalDateTime createdAt;
}