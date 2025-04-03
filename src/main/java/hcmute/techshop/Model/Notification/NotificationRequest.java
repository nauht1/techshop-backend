package hcmute.techshop.Model.Notification;

import hcmute.techshop.Enum.NotificationType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificationRequest {
    private String title;
    private String message;
    private NotificationType type;
    private Integer userId;
}