package hcmute.techshop.Repository.Notification;

import hcmute.techshop.Entity.Auth.UserEntity;
import hcmute.techshop.Entity.Notification.NotificationEntity;
import hcmute.techshop.Enum.NotificationType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<NotificationEntity, Integer> {
    List<NotificationEntity> findByUserOrderByCreatedAtDesc(UserEntity user);

    List<NotificationEntity> findByUserAndIsReadOrderByCreatedAtDesc(UserEntity user, boolean isRead);

    @Query("SELECT COUNT(n) FROM NotificationEntity n WHERE n.user = ?1 AND n.isRead = false")
    Long countUnreadNotifications(UserEntity user);

    List<NotificationEntity> findByUserAndTypeOrderByCreatedAtDesc(UserEntity user, NotificationType type);
}