package hcmute.techshop.Service.Tracking;

import hcmute.techshop.Entity.Auth.UserEntity;
import hcmute.techshop.Entity.Auth.UserTracking;
import hcmute.techshop.Enum.EventType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDateTime;
import java.util.List;

public interface ITrackingService {
    Page<UserTracking> getAllTrackings(PageRequest pageRequest, Long userId, Long productId, LocalDateTime startDate, LocalDateTime endDate);
    UserTracking getDetailTracking(Long id);
    void deleteTracking(Long id);
    void track(UserEntity user, EventType eventType, String eventData);
}
