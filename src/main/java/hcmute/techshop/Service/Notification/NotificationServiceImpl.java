package hcmute.techshop.Service.Notification;

import hcmute.techshop.Entity.Auth.UserEntity;
import hcmute.techshop.Entity.Notification.NotificationEntity;
import hcmute.techshop.Enum.NotificationType;
import hcmute.techshop.Exception.ResourceNotFoundException;
import hcmute.techshop.Model.Notification.NotificationModel;
import hcmute.techshop.Repository.Notification.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements INotificationService {

    private final NotificationRepository notificationRepository;

    @Override
    public List<NotificationModel> getAllNotifications(UserEntity user) {
        List<NotificationEntity> notifications = notificationRepository.findByUserOrderByCreatedAtDesc(user);
        return notifications.stream()
                .map(this::mapToModel)
                .collect(Collectors.toList());
    }

    @Override
    public List<NotificationModel> getUnreadNotifications(UserEntity user) {
        List<NotificationEntity> notifications = notificationRepository.findByUserAndIsReadOrderByCreatedAtDesc(user, false);
        return notifications.stream()
                .map(this::mapToModel)
                .collect(Collectors.toList());
    }

    @Override
    public List<NotificationModel> getNotificationsByType(UserEntity user, NotificationType type) {
        List<NotificationEntity> notifications = notificationRepository.findByUserAndTypeOrderByCreatedAtDesc(user, type);
        return notifications.stream()
                .map(this::mapToModel)
                .collect(Collectors.toList());
    }

    @Override
    public NotificationModel getNotificationById(Integer id, UserEntity user) {
        NotificationEntity notification = notificationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Thông báo không tồn tại"));

        // Check if notification belongs to user
        if (!notification.getUser().getId().equals(user.getId())) {
            throw new IllegalArgumentException("Bạn không có quyền xem thông báo này");
        }

        return mapToModel(notification);
    }

    @Override
    @Transactional
    public void deleteNotification(Integer id, UserEntity user) {
        NotificationEntity notification = notificationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Thông báo không tồn tại"));

        // Check if notification belongs to user
        if (!notification.getUser().getId().equals(user.getId())) {
            throw new IllegalArgumentException("Bạn không có quyền xóa thông báo này");
        }

        notificationRepository.delete(notification);
    }

    @Override
    @Transactional
    public void markAsRead(List<Integer> notificationIds, UserEntity user) {
        for (Integer id : notificationIds) {
            NotificationEntity notification = notificationRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Thông báo không tồn tại với ID: " + id));

            // Check if notification belongs to user
            if (!notification.getUser().getId().equals(user.getId())) {
                throw new IllegalArgumentException("Bạn không có quyền đánh dấu thông báo này");
            }

            notification.setRead(true);
            notificationRepository.save(notification);
        }
    }

    @Override
    @Transactional
    public void markAllAsRead(UserEntity user) {
        List<NotificationEntity> unreadNotifications = notificationRepository.findByUserAndIsReadOrderByCreatedAtDesc(user, false);

        for (NotificationEntity notification : unreadNotifications) {
            notification.setRead(true);
            notificationRepository.save(notification);
        }
    }

    @Override
    public Long getUnreadCount(UserEntity user) {
        return notificationRepository.countUnreadNotifications(user);
    }

    @Override
    @Transactional
    public NotificationEntity sendNotification(UserEntity user, String title, String message, NotificationType type) {
        NotificationEntity notification = new NotificationEntity();
        notification.setUser(user);
        notification.setTitle(title);
        notification.setMessage(message);
        notification.setType(type);
        notification.setRead(false);
        notification.setCreatedAt(LocalDateTime.now());

        return notificationRepository.save(notification);
    }

    @Override
    @Transactional
    public List<NotificationEntity> sendNotificationToUsers(List<UserEntity> users, String title, String message, NotificationType type) {
        List<NotificationEntity> notifications = new ArrayList<>();

        for (UserEntity user : users) {
            NotificationEntity notification = sendNotification(user, title, message, type);
            notifications.add(notification);
        }

        return notifications;
    }

    private NotificationModel mapToModel(NotificationEntity entity) {
        return NotificationModel.builder()
                .id(entity.getId())
                .userId(entity.getUser().getId())
                .title(entity.getTitle())
                .message(entity.getMessage())
                .isRead(entity.isRead())
                .type(entity.getType())
                .createdAt(entity.getCreatedAt())
                .build();
    }
}