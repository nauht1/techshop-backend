package hcmute.techshop.Entity;

import hcmute.techshop.Enum.NotificationType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "notifications")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    private String title;
    private String message;
    private boolean isRead;

    @Enumerated(EnumType.STRING)
    private NotificationType type;

    private LocalDateTime createdAt;
}
