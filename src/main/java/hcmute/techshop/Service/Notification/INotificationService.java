package hcmute.techshop.Service.Notification;

import hcmute.techshop.Entity.Auth.UserEntity;
import hcmute.techshop.Entity.Notification.NotificationEntity;
import hcmute.techshop.Enum.NotificationType;
import hcmute.techshop.Model.Notification.NotificationModel;

import java.util.List;

public interface INotificationService {

    List<NotificationModel> getAllNotifications(UserEntity user);

    List<NotificationModel> getUnreadNotifications(UserEntity user);

    List<NotificationModel> getNotificationsByType(UserEntity user, NotificationType type);

    NotificationModel getNotificationById(Integer id, UserEntity user);

    void deleteNotification(Integer id, UserEntity user);

    void markAsRead(List<Integer> notificationIds, UserEntity user);

    void markAllAsRead(UserEntity user);

    Long getUnreadCount(UserEntity user);

    NotificationEntity sendNotification(
            UserEntity user,
            String title,
            String message,
            NotificationType type);

    List<NotificationEntity> sendNotificationToUsers(
            List<UserEntity> users,
            String title,
            String message,
            NotificationType type);
}