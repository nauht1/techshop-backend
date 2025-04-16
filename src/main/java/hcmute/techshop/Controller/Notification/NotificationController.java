package hcmute.techshop.Controller.Notification;

import hcmute.techshop.Entity.Auth.UserEntity;
import hcmute.techshop.Entity.Notification.NotificationEntity;
import hcmute.techshop.Enum.NotificationType;
import hcmute.techshop.Model.ApiResponse;
import hcmute.techshop.Model.Notification.MarkReadRequest;
import hcmute.techshop.Model.Notification.NotificationCountResponse;
import hcmute.techshop.Model.Notification.NotificationModel;
import hcmute.techshop.Model.Notification.NotificationRequest;
import hcmute.techshop.Repository.Auth.UserRepository;
import hcmute.techshop.Service.Notification.INotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final INotificationService notificationService;
    private final UserRepository userRepository;

    @GetMapping
    public ResponseEntity<ApiResponse<List<NotificationModel>>> getAllNotifications(
            @AuthenticationPrincipal UserEntity user) {
        try {
            List<NotificationModel> notifications = notificationService.getAllNotifications(user);
            return ResponseEntity.ok(new ApiResponse<>(true, "Lấy danh sách thông báo thành công", notifications));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Lỗi khi lấy danh sách thông báo: " + e.getMessage(), null));
        }
    }

    @GetMapping("/unread")
    public ResponseEntity<ApiResponse<List<NotificationModel>>> getUnreadNotifications(
            @AuthenticationPrincipal UserEntity user) {
        try {
            List<NotificationModel> notifications = notificationService.getUnreadNotifications(user);
            return ResponseEntity.ok(new ApiResponse<>(true, "Lấy danh sách thông báo chưa đọc thành công", notifications));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Lỗi khi lấy danh sách thông báo chưa đọc: " + e.getMessage(), null));
        }
    }

    @GetMapping("/count")
    public ResponseEntity<ApiResponse<NotificationCountResponse>> getUnreadCount(
            @AuthenticationPrincipal UserEntity user) {
        try {
            Long count = notificationService.getUnreadCount(user);
            NotificationCountResponse response = new NotificationCountResponse(count);
            return ResponseEntity.ok(new ApiResponse<>(true, "Lấy số lượng thông báo chưa đọc thành công", response));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Lỗi khi lấy số lượng thông báo chưa đọc: " + e.getMessage(), null));
        }
    }

    @GetMapping("/type/{type}")
    public ResponseEntity<ApiResponse<List<NotificationModel>>> getNotificationsByType(
            @AuthenticationPrincipal UserEntity user,
            @PathVariable NotificationType type) {
        try {
            List<NotificationModel> notifications = notificationService.getNotificationsByType(user, type);
            return ResponseEntity.ok(new ApiResponse<>(true, "Lấy danh sách thông báo theo loại thành công", notifications));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Lỗi khi lấy danh sách thông báo theo loại: " + e.getMessage(), null));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<NotificationModel>> getNotificationById(
            @AuthenticationPrincipal UserEntity user,
            @PathVariable Integer id) {
        try {
            NotificationModel notification = notificationService.getNotificationById(id, user);
            return ResponseEntity.ok(new ApiResponse<>(true, "Lấy thông báo thành công", notification));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Lỗi khi lấy thông báo: " + e.getMessage(), null));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteNotification(
            @AuthenticationPrincipal UserEntity user,
            @PathVariable Integer id) {
        try {
            notificationService.deleteNotification(id, user);
            return ResponseEntity.ok(new ApiResponse<>(true, "Xóa thông báo thành công", null));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Lỗi khi xóa thông báo: " + e.getMessage(), null));
        }
    }

    @PutMapping("/read")
    public ResponseEntity<ApiResponse<Void>> markAsRead(
            @AuthenticationPrincipal UserEntity user,
            @RequestBody MarkReadRequest request) {
        try {
            notificationService.markAsRead(request.getNotificationIds(), user);
            return ResponseEntity.ok(new ApiResponse<>(true, "Đánh dấu đã đọc thành công", null));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Lỗi khi đánh dấu đã đọc: " + e.getMessage(), null));
        }
    }

    @PutMapping("/read-all")
    public ResponseEntity<ApiResponse<Void>> markAllAsRead(
            @AuthenticationPrincipal UserEntity user) {
        try {
            notificationService.markAllAsRead(user);
            return ResponseEntity.ok(new ApiResponse<>(true, "Đánh dấu tất cả đã đọc thành công", null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Lỗi khi đánh dấu tất cả đã đọc: " + e.getMessage(), null));
        }
    }

    @PostMapping("/admin/send")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> sendNotification(
            @RequestBody NotificationRequest request) {
        try {
            if (request.getUserId() != null) {
                // Send to specific user
                UserEntity user = userRepository.findById(request.getUserId())
                        .orElseThrow(() -> new IllegalArgumentException("Người dùng không tồn tại"));

                NotificationEntity notification = notificationService.sendNotification(
                        user,
                        request.getTitle(),
                        request.getMessage(),
                        request.getType()
                );

                return ResponseEntity.ok(new ApiResponse<>(true, "Gửi thông báo thành công", null));
            } else {
                // Send to all users
                List<UserEntity> users = userRepository.findAll();

                List<NotificationEntity> notifications = notificationService.sendNotificationToUsers(
                        users,
                        request.getTitle(),
                        request.getMessage(),
                        request.getType()
                );

                return ResponseEntity.ok(new ApiResponse<>(true, "Gửi thông báo đến tất cả người dùng thành công", null));
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Lỗi khi gửi thông báo: " + e.getMessage(), null));
        }
    }
}